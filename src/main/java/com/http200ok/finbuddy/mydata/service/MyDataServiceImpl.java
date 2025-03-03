package com.http200ok.finbuddy.mydata.service;


import com.http200ok.finbuddy.account.domain.Account;
import com.http200ok.finbuddy.account.domain.AccountType;
import com.http200ok.finbuddy.account.repository.AccountRepository;
import com.http200ok.finbuddy.bank.domain.Bank;
import com.http200ok.finbuddy.bank.repository.BankRepository;
import com.http200ok.finbuddy.category.repository.CategoryRepository;
import com.http200ok.finbuddy.member.domain.Member;
import com.http200ok.finbuddy.member.repository.MemberRepository;
import com.http200ok.finbuddy.mydata.dto.MyDataDeletionResult;
import com.http200ok.finbuddy.mydata.dto.MyDataGenerationResult;
import com.http200ok.finbuddy.product.domain.*;
import com.http200ok.finbuddy.product.repository.CheckingProductRepository;
import com.http200ok.finbuddy.product.repository.DepositProductRepository;
import com.http200ok.finbuddy.product.repository.SavingProductRepository;
import com.http200ok.finbuddy.transaction.domain.Transaction;
import com.http200ok.finbuddy.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyDataServiceImpl implements MyDataService {

    // 레포지토리 의존성
    private final MemberRepository memberRepository;
    private final BankRepository bankRepository;
    private final AccountRepository accountRepository;
    private final DepositProductRepository depositProductRepository;
    private final SavingProductRepository savingProductRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;
    private final CheckingProductRepository checkingProductRepository;

    // 가게 이름 목록
    private final Map<Long, List<String>> categoryStores = new HashMap<>();

    // 지출/수입 거래 유형 (1: 입금, 2: 출금)
    private static final int INCOME_TYPE = 1;
    private static final int EXPENSE_TYPE = 2;

    @Override
    @Transactional
    public MyDataGenerationResult generateDummyDataForMember(Long memberId) {
        try {
            // 카테고리별 가게 정보 초기화
            initCategoryStores();

            // 회원 찾기
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다: " + memberId));

            System.out.println("회원 " + member.getName() + "(ID: " + member.getId() + ")에 대한 더미 데이터 생성 시작");

            // 계좌 및 거래내역 생성
            List<Account> checkingAccounts = generateCheckingAccounts(member);
            Account mainAccount = checkingAccounts.getFirst();

            List<Account> depositAccounts = generateDepositAccounts(member);
            List<Account> savingAccounts = generateSavingAccounts(member);

            int transactionCount = generateTransactionsForMainAccount(mainAccount);
            transactionCount += generateInterAccountTransactions(mainAccount, checkingAccounts, depositAccounts, savingAccounts);

            System.out.println("회원 " + member.getName() + "(ID: " + member.getId() + ")의 더미 데이터 생성 완료");

            // 결과 반환
            return MyDataGenerationResult.createResult(
                    member.getId(),
                    member.getName(),
                    checkingAccounts.size(),
                    depositAccounts.size(),
                    savingAccounts.size(),
                    transactionCount,
                    true,
                    "데이터 생성 완료"
            );

        } catch (Exception e) {
            System.err.println("회원 ID " + memberId + "에 대한 더미 데이터 생성 중 오류 발생: " + e.getMessage());
            e.printStackTrace();

            return MyDataGenerationResult.createResult(
                    memberId,
                    null,
                    0,
                    0,
                    0,
                    0,
                    false,
                    "오류 발생: " + e.getMessage()
            );
        }
    }

    @Override
    @Transactional
    public MyDataDeletionResult deleteExistingDataForMember(Long memberId) {
        try {
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다: " + memberId));

            // 회원의 기존 계좌 찾기
            List<Account> existingAccounts = accountRepository.findByMemberId(memberId);
            int accountCount = existingAccounts.size();
            int transactionCount = 0;

            // 계좌별 거래내역 삭제
            for (Account account : existingAccounts) {
                List<Transaction> transactions = transactionRepository.findByAccountId(account.getId());
                transactionCount += transactions.size();
                transactionRepository.deleteByAccountId(account.getId());
            }

            // 계좌 삭제
            accountRepository.deleteById(memberId);

            System.out.println("회원 " + member.getName() + "(ID: " + memberId + ")의 기존 데이터 삭제 완료. 계좌 "
                    + accountCount + "개, 거래내역 " + transactionCount + "개 삭제됨");

            return MyDataDeletionResult.createResult(
                    memberId,
                    member.getName(),
                    accountCount,
                    transactionCount,
                    true,
                    "데이터 삭제 완료"
            );

        } catch (Exception e) {
            System.err.println("회원 ID " + memberId + "의 기존 데이터 삭제 중 오류 발생: " + e.getMessage());
            e.printStackTrace();

            return MyDataDeletionResult.createResult(
                    memberId,
                    null,
                    0,
                    0,
                    false,
                    "오류 발생: " + e.getMessage()
            );
        }
    }

    private void initCategoryStores() {
        // 식비 (카테고리 ID: 1)
        categoryStores.put(1L, Arrays.asList(
                "맥도날드", "버거킹", "롯데리아", "KFC", "아웃백", "VIPS", "서브웨이",
                "김밥천국", "본죽", "교촌치킨", "BBQ", "BHC", "피자헛", "도미노피자",
                "명인만두", "본죽&비빔밥", "이디야 카페", "CU", "GS25", "세븐일레븐"
        ));

        // 쇼핑 (카테고리 ID: 2)
        categoryStores.put(2L, Arrays.asList(
                "유니클로", "자라", "H&M", "나이키", "아디다스", "무신사", "올리브영",
                "롯데백화점", "신세계백화점", "현대백화점", "이마트", "홈플러스", "코스트코",
                "쿠팡", "G마켓", "11번가", "SSG닷컴", "티몬", "위메프", "ABC마트"
        ));

        // 오락 (카테고리 ID: 3)
        categoryStores.put(3L, Arrays.asList(
                "CGV", "메가박스", "롯데시네마", "넷플릭스", "왓챠", "디즈니플러스",
                "티빙", "스포티파이", "멜론", "지니뮤직", "플로", "Xbox Game Pass",
                "PlayStation Store", "스팀", "닌텐도", "NEXON", "NC SOFT", "VR ZONE"
        ));

        // 카페 (카테고리 ID: 4)
        categoryStores.put(4L, Arrays.asList(
                "스타벅스", "투썸플레이스", "이디야", "폴바셋", "커피빈", "할리스",
                "파스쿠찌", "엔제리너스", "매머드커피", "빽다방", "컴포즈커피", "더벤티",
                "메가커피", "달콤커피", "공차", "요거프레소", "블루보틀", "폴인커피"
        ));

        // 교통 (카테고리 ID: 5)
        categoryStores.put(5L, Arrays.asList(
                "서울교통공사", "카카오T", "타다", "우버", "쏘카", "그린카",
                "한국철도공사", "SRT", "티머니", "캐시비", "레일플러스", "코레일"
        ));

        // 주거/통신 (카테고리 ID: 6)
        categoryStores.put(6L, Arrays.asList(
                "한국전력공사", "서울도시가스", "경동도시가스", "가스공사", "한국수자원공사",
                "SKT", "KT", "LG U+", "SK브로드밴드", "LG헬로비전", "월세", "관리비"
        ));

        // 기타 (카테고리 ID: 7)
        categoryStores.put(7L, Arrays.asList(
                "이체", "ATM 출금", "보험료", "택배비", "구독료", "세금", "적금", "예금",
                "의료비", "교육비", "경조사비", "기부금", "자산", "용돈", "급여"
        ));
    }

    // getRandomCheckingProductForBank 이용해서 product에 있는 checking 생성으로 수정해야 함
    private List<Account> generateCheckingAccounts(Member member) {
        List<Account> accounts = new ArrayList<>();
        List<Bank> banks = bankRepository.findAll();
        List<CheckingProduct> checkingProducts = checkingProductRepository.findAll();

        // 랜덤하게 은행 선택 (중복 없이)
        List<Bank> selectedBanks = getRandomElements(banks, 5);

        // 메인 계좌 먼저 생성 (잔액이 더 많고 거래가 많은 계좌)
        Bank mainBank = selectedBanks.getFirst();
        CheckingProduct mainProduct = getRandomCheckingProductForBank(checkingProducts, mainBank.getId());

        Account mainAccount = Account.createAccount(
                member,
                mainBank,
                mainProduct,
                null,
                mainProduct.getName(),
                "CH" + generateRandomDigits(8),
                "1234",
                AccountType.CHECKING,
                randomAmount(1500, 3000) * 1000,
                randomDateWithinLastYear(),
                null
        );

        accounts.add(accountRepository.save(mainAccount));

        // 나머지 입출금 계좌 생성
        for (int i = 1; i < 5; i++) {
            Bank bank = selectedBanks.get(i);
            CheckingProduct product = getRandomCheckingProductForBank(checkingProducts, bank.getId());

            Account account = Account.createAccount(
                    member,
                    bank,
                    product,
                    null,
                    product.getName(),
                    "CH" + generateRandomDigits(8),
                    "1234",
                    AccountType.CHECKING,
                    randomAmount(50, 500) * 1000,
                    randomDateWithinLastYear(),
                    null
            );

            accounts.add(accountRepository.save(account));
        }

        return accounts;
    }

    private List<Account> generateDepositAccounts(Member member) {
        List<Account> accounts = new ArrayList<>();
        List<Bank> banks = bankRepository.findAll();
        List<DepositProduct> depositProducts = depositProductRepository.findAll();

        // 랜덤하게 은행 선택 (중복 없이)
        List<Bank> selectedBanks = getRandomElements(banks, 2);

        for (int i = 0; i < 2; i++) {
            Bank bank = selectedBanks.get(i);
            DepositProduct product = getRandomDepositProductForBank(depositProducts, bank.getId());

            if (product != null && !product.getOptions().isEmpty()) {
                // 상품 옵션 중 하나를 랜덤하게 선택
                DepositProductOption option = getRandomElement(product.getOptions());

                // 생성 날짜와 만기 날짜 설정
                LocalDateTime createdAt = randomDateWithinLastYear();
                LocalDateTime maturedAt;

                // 만기 날짜는 생성 날짜로부터 option의 saving_term 개월 후
                if (option != null && option.getSavingTerm() != null) {
                    maturedAt = createdAt.plusMonths(option.getSavingTerm());
                } else {
                    maturedAt = createdAt.plusYears(1); // 기본값 1년
                }

                // 예금 계좌의 잔액 설정 (500만원~2000만원) - 만원 단위로 조정
                long balance = (randomAmount(500, 2000) * 10000);

                Account account = Account.createAccount(
                        member,
                        bank,
                        product,
                        option,
                        product.getName(), // 상품 이름 사용
                        "DP" + generateRandomDigits(8),
                        "pass" + generateRandomDigits(3),
                        AccountType.DEPOSIT,
                        balance,
                        createdAt,
                        maturedAt
                );

                accounts.add(accountRepository.save(account));
            }
        }

        return accounts;
    }

    private List<Account> generateSavingAccounts(Member member) {
        List<Account> accounts = new ArrayList<>();
        List<Bank> banks = bankRepository.findAll();
        List<SavingProduct> savingProducts = savingProductRepository.findAll();

        // 랜덤하게 은행 선택 (중복 없이)
        List<Bank> selectedBanks = getRandomElements(banks, 2);

        for (int i = 0; i < 2; i++) {
            Bank bank = selectedBanks.get(i);
            SavingProduct product = getRandomSavingProductForBank(savingProducts, bank.getId());

            if (product != null && !product.getOptions().isEmpty()) {
                // 상품 옵션 중 하나를 랜덤하게 선택
                SavingProductOption option = getRandomElement(product.getOptions());

                // 생성 날짜와 만기 날짜 설정
                LocalDateTime createdAt = randomDateWithinLastYear();
                LocalDateTime maturedAt;

                // 만기 날짜는 생성 날짜로부터 option의 saving_term 개월 후
                if (option != null && option.getSavingTerm() != null) {
                    maturedAt = createdAt.plusMonths(option.getSavingTerm());
                } else {
                    maturedAt = createdAt.plusYears(1); // 기본값 1년
                }

                // 적금 계좌의 잔액 설정 (50만원~500만원) - 만원 단위로 조정
                long balance = (randomAmount(50, 500) * 10000);

                Account account = Account.createAccount(
                        member,
                        bank,
                        product,
                        option,
                        product.getName(), // 상품 이름 사용
                        "SV" + generateRandomDigits(8),
                        "pass" + generateRandomDigits(3),
                        AccountType.SAVING,
                        balance,
                        createdAt,
                        maturedAt
                );

                accounts.add(accountRepository.save(account));
            }
        }

        return accounts;
    }

    private int generateTransactionsForMainAccount(Account mainAccount) {
        // 초기 잔액 설정 - 현재 계좌 잔액 사용
        long currentBalance = mainAccount.getBalance();

        // 기준 날짜 설정 (오늘로부터 60일 전)
        LocalDateTime baseDate = LocalDateTime.now().minusDays(60);

        // 수입/지출 거래 생성에 사용할 목록
        List<Transaction> transactions = new ArrayList<>();

        // 거래 생성에 필요한 잔액 트래킹을 위한 임시 변수
        long tempBalance = 0;

        // 급여 입금 (매월 25일 정도)
        // 먼저 수입 거래를 생성하고 초기 잔액 설정
        for (int i = 0; i < 3; i++) {
            LocalDateTime salaryDate = baseDate.plusDays(i * 30).withDayOfMonth(25);
            if (salaryDate.isAfter(LocalDateTime.now())) {
                continue; // 미래 날짜는 건너뜀
            }

            // 급여는 1000원 단위로 설정
            long salaryAmount = randomAmount(2500, 3500) * 1000;
            tempBalance += salaryAmount;

            Transaction salaryTx = Transaction.createDummyTransaction(
                    mainAccount,
                    INCOME_TYPE,
                    salaryAmount,
                    categoryRepository.findById(7L).orElse(null), // 기타 카테고리
                    salaryDate,
                    getRandomCompanyName(),
                    tempBalance // 최신화된 잔액 설정
            );

            transactions.add(salaryTx);
        }

        // 기타 수입 (용돈, 환급금 등)
        for (int i = 0; i < randomInt(3, 6); i++) {
            LocalDateTime incomeDate = randomDateBetween(baseDate, LocalDateTime.now());
            // 1000원 단위로 설정
            long incomeAmount = randomAmount(50, 300) * 1000;
            tempBalance += incomeAmount;

            Transaction incomeTx = Transaction.createDummyTransaction(
                    mainAccount,
                    INCOME_TYPE,
                    incomeAmount,
                    categoryRepository.findById(7L).orElse(null), // 기타 카테고리
                    incomeDate,
                    getRandomPersonName(),
                    tempBalance // 최신화된 잔액 설정
            );

            transactions.add(incomeTx);
        }

        // 지출 거래 - 각 카테고리별로 생성
        for (long categoryId = 1; categoryId <= 7; categoryId++) {
            int transactionCount = categoryId == 7 ? randomInt(2, 5) : randomInt(5, 15);

            for (int i = 0; i < transactionCount; i++) {
                LocalDateTime expenseDate = randomDateBetween(baseDate, LocalDateTime.now());
                // 카테고리별 금액 설정 - 1000원 단위로 조정
                long expenseAmount = (getRandomAmountForCategory(categoryId) / 1000) * 1000;

                // 잔액이 충분한지 확인
                if (tempBalance - expenseAmount < 0) {
                    // 잔액이 부족하면 지출 금액 조정
                    expenseAmount = tempBalance > 1000 ? tempBalance - 1000 : 0;
                    if (expenseAmount <= 0) {
                        continue; // 지출할 금액이 없으면 건너뜀
                    }
                }

                tempBalance -= expenseAmount;

                Transaction expenseTx = Transaction.createDummyTransaction(
                        mainAccount,
                        EXPENSE_TYPE,
                        expenseAmount,
                        categoryRepository.findById(categoryId).orElse(null),
                        expenseDate,
                        getRandomStoreForCategory(categoryId),
                        tempBalance // 최신화된 잔액 설정
                );

                transactions.add(expenseTx);
            }
        }

        // 거래 날짜 기준 정렬
        transactions.sort(Comparator.comparing(Transaction::getTransactionDate));

        // 잔액 다시 계산 (가장 오래된 거래부터)
        long runningBalance = 0;
        for (Transaction tx : transactions) {
            if (tx.getTransactionType() == INCOME_TYPE) {
                runningBalance += tx.getAmount();
            } else {
                runningBalance -= tx.getAmount();
            }
            tx.setUpdatedBalance(runningBalance);
        }

        // 저장
        transactionRepository.saveAll(transactions);

        // 최종 잔액 업데이트
        mainAccount.setBalance(runningBalance);
        accountRepository.save(mainAccount);

        return transactions.size();
    }

    // 수정 해야함
    private int generateInterAccountTransactions(Account mainAccount, List<Account> checkingAccounts,
                                                 List<Account> depositAccounts, List<Account> savingAccounts) {
        List<Transaction> allTransactions = new ArrayList<>();
        long transferAmount = randomAmount(50, 200) * 1000;

        // 메인 계좌 -> 다른 입출금 계좌 이체
        if (mainAccount.getBalance() >= transferAmount) {
            for (int i = 1; i < checkingAccounts.size(); i++) {
                Account targetAccount = checkingAccounts.get(i);
                for (int j = 0; j < randomInt(1, 3); j++) {
                    LocalDateTime transferDate = randomDateWithinLastMonth();

                    // 메인 계좌에서 출금
                    Transaction outTx = Transaction.createDummyTransaction(
                            mainAccount,
                            EXPENSE_TYPE,
                            transferAmount,
                            categoryRepository.findById(7L).orElse(null), // 기타(이체)
                            transferDate,
                            targetAccount.getMember().getName(),
                            mainAccount.getBalance() - transferAmount
                    );

                    // 타겟 계좌에 입금
                    Transaction inTx = Transaction.createDummyTransaction(
                            targetAccount,
                            INCOME_TYPE,
                            transferAmount,
                            categoryRepository.findById(7L).orElse(null), // 기타(이체)
                            transferDate,
                            mainAccount.getMember().getName(),
                            0L // 잔액은 나중에 계산
                    );

                    allTransactions.add(outTx);
                    allTransactions.add(inTx);
                }
            }
        }

        // 적금 계좌 정기 납입
        for (Account savingAccount : savingAccounts) {
            // 적금 시작일부터 현재까지 매월 납입
            LocalDateTime startDate = savingAccount.getCreatedAt();
            LocalDateTime now = LocalDateTime.now();

            // 매월 납입한 횟수 계산
            long monthsBetween = startDate.toLocalDate().until(now.toLocalDate()).toTotalMonths();
            long monthlyDeposit = savingAccount.getBalance() / (monthsBetween > 0 ? monthsBetween : 1);

            for (int i = 0; i < monthsBetween; i++) {
                LocalDateTime depositDate = startDate.plusMonths(i);

                // 메인 계좌에서 출금
                Transaction outTx = Transaction.createDummyTransaction(
                        mainAccount,
                        EXPENSE_TYPE,
                        monthlyDeposit,
                        categoryRepository.findById(7L).orElse(null), // 기타(적금)
                        depositDate,
                        savingAccount.getMember().getName(),
                        0L // 임시값
                );

                // 적금 계좌에 입금
                Transaction inTx = Transaction.createDummyTransaction(
                        savingAccount,
                        INCOME_TYPE,
                        monthlyDeposit,
                        categoryRepository.findById(7L).orElse(null), // 기타(적금)
                        depositDate,
                        mainAccount.getMember().getName(),
                        0L // 임시값
                );

                allTransactions.add(outTx);
                allTransactions.add(inTx);
            }
        }

        // 예금 계좌 일시 납입
        for (Account depositAccount : depositAccounts) {
            LocalDateTime depositDate = depositAccount.getCreatedAt();

            // 메인 계좌에서 출금
            Transaction outTx = Transaction.createDummyTransaction(
                    mainAccount,
                    EXPENSE_TYPE,
                    depositAccount.getBalance(),
                    categoryRepository.findById(7L).orElse(null), // 기타(예금)
                    depositDate,
                    depositAccount.getMember().getName(),
                    0L // 임시값
            );

            // 예금 계좌에 입금
            Transaction inTx = Transaction.createDummyTransaction(
                    depositAccount,
                    INCOME_TYPE,
                    depositAccount.getBalance(),
                    categoryRepository.findById(7L).orElse(null), // 기타(예금)
                    depositDate,
                    mainAccount.getMember().getName(),
                    0L // 임시값
            );

            allTransactions.add(outTx);
            allTransactions.add(inTx);
        }

        // 거래 날짜 기준 정렬
        allTransactions.sort(Comparator.comparing(Transaction::getTransactionDate));

        // 계좌별 잔액 재계산
        Map<Long, Long> accountBalances = new HashMap<>();
        for (Account account : checkingAccounts) {
            accountBalances.put(account.getId(), 0L);
        }
        for (Account account : depositAccounts) {
            accountBalances.put(account.getId(), 0L);
        }
        for (Account account : savingAccounts) {
            accountBalances.put(account.getId(), 0L);
        }

        for (Transaction tx : allTransactions) {
            Long accountId = tx.getAccount().getId();
            Long currentBalance = accountBalances.getOrDefault(accountId, 0L);

            if (tx.getTransactionType() == INCOME_TYPE) {
                currentBalance += tx.getAmount();
            } else {
                currentBalance -= tx.getAmount();
            }

            accountBalances.put(accountId, currentBalance);
            tx.setUpdatedBalance(currentBalance);
        }

        // 저장
        transactionRepository.saveAll(allTransactions);

        return allTransactions.size();
    }

    // 유틸리티 메서드들
    private <T> List<T> getRandomElements(List<T> list, int count) {
        List<T> copy = new ArrayList<>(list);
        Collections.shuffle(copy);
        return copy.stream().limit(count).collect(Collectors.toList());
    }

    private <T> T getRandomElement(List<T> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        int index = (int) (Math.random() * list.size());
        return list.get(index);
    }

    private DepositProduct getRandomDepositProductForBank(List<DepositProduct> products, Long bankId) {
        List<DepositProduct> bankProducts = products.stream()
                .filter(p -> p.getBank() != null && p.getBank().getId().equals(bankId))
                .collect(Collectors.toList());

        if (bankProducts.isEmpty()) {
            return null;
        }

        return getRandomElement(bankProducts);
    }

    private SavingProduct getRandomSavingProductForBank(List<SavingProduct> products, Long bankId) {
        List<SavingProduct> bankProducts = products.stream()
                .filter(p -> p.getBank() != null && p.getBank().getId().equals(bankId))
                .collect(Collectors.toList());

        if (bankProducts.isEmpty()) {
            return null;
        }

        return getRandomElement(bankProducts);
    }

    private CheckingProduct getRandomCheckingProductForBank(List<CheckingProduct> products, Long bankId) {
        List<CheckingProduct> bankProducts = products.stream()
                .filter(p -> p.getBank() != null && p.getBank().getId().equals(bankId))
                .collect(Collectors.toList());

        if (bankProducts.isEmpty()) {
            return null;
        }

        return getRandomElement(bankProducts);
    }

    private String getRandomCheckingAccountName(String bankName) {
        String[] prefixes = {"급여", "생활비", "용돈", "주거래", "비상금", "여행", "쇼핑"};
        String prefix = prefixes[(int) (Math.random() * prefixes.length)];
        return bankName + " " + prefix + "통장";
    }

    private String generateRandomDigits(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append((int) (Math.random() * 10));
        }
        return sb.toString();
    }

    private long randomAmount(long min, long max) {
        return min + (long) (Math.random() * (max - min + 1));
    }

    private int randomInt(int min, int max) {
        return min + (int) (Math.random() * (max - min + 1));
    }

    private LocalDateTime randomDateWithinLastYear() {
        LocalDateTime now = LocalDateTime.now();
        long daysToSubtract = (long) (Math.random() * 365) + 1;
        return now.minusDays(daysToSubtract);
    }

    private LocalDateTime randomDateWithinLastMonth() {
        LocalDateTime now = LocalDateTime.now();
        long daysToSubtract = (long) (Math.random() * 30) + 1;
        return now.minusDays(daysToSubtract);
    }

    private LocalDateTime randomDateBetween(LocalDateTime start, LocalDateTime end) {
        long startEpochDay = start.atZone(ZoneId.systemDefault()).toLocalDate().toEpochDay();
        long endEpochDay = end.atZone(ZoneId.systemDefault()).toLocalDate().toEpochDay();
        long daysBetween = endEpochDay - startEpochDay;
        long randomDays = (long) (Math.random() * daysBetween);

        LocalDate date = LocalDate.ofEpochDay(startEpochDay + randomDays);
        int hour = (int) (Math.random() * 14) + 8; // 8시~22시 사이
        int minute = (int) (Math.random() * 60);

        return LocalDateTime.of(date, java.time.LocalTime.of(hour, minute));
    }

    private String getRandomCompanyName() {
        String[] companies = {
                "(주)삼성전자", "(주)현대자동차", "(주)LG전자", "(주)SK텔레콤", "(주)네이버",
                "(주)카카오", "(주)쿠팡", "(주)우아한형제들", "(주)당근마켓", "(주)토스",
                "(주)라인", "(주)배달의민족", "(주)야놀자", "(주)직방", "(주)마이리얼트립"
        };
        return companies[(int) (Math.random() * companies.length)];
    }

    private String getRandomPersonName() {
        String[] firstNames = {"김", "이", "박", "최", "정", "강", "조", "윤", "장", "임", "한", "오", "서", "신", "권", "황", "안", "송", "전", "홍"};
        String[] lastNames = {"민준", "서준", "예준", "도윤", "시우", "주원", "하준", "지호", "준서", "준우",
                "서연", "서윤", "지우", "서현", "민서", "하은", "하윤", "윤서", "지민", "채원"};

        return firstNames[(int) (Math.random() * firstNames.length)] + lastNames[(int) (Math.random() * lastNames.length)];
    }

    private String getRandomStoreForCategory(Long categoryId) {
        List<String> stores = categoryStores.getOrDefault(categoryId, new ArrayList<>());
        if (stores.isEmpty()) {
            return "일반 가맹점";
        }

        String store = stores.get((int) (Math.random() * stores.size()));

        // 지점명 추가 (50% 확률)
        if ((int) (Math.random() * 2) == 1) {
            String[] locations = {"강남점", "홍대점", "명동점", "신촌점", "종로점", "여의도점", "잠실점", "용산점", "건대점", "영등포점"};
            store += " " + locations[(int) (Math.random() * locations.length)];
        }

        return store;
    }

    private long getRandomAmountForCategory(Long categoryId) {
        return switch (categoryId.intValue()) {
            case 1 -> // 식비
                    randomAmount(5000, 50000);
            case 2 -> // 쇼핑
                    randomAmount(10000, 200000);
            case 3 -> // 오락
                    randomAmount(10000, 100000);
            case 4 -> // 카페
                    randomAmount(4000, 15000);
            case 5 -> // 교통
                    randomAmount(1250, 30000);
            case 6 -> // 주거/통신
                    randomAmount(50000, 1000000);
            case 7 -> // 기타
                    randomAmount(10000, 200000);
            default -> randomAmount(1000, 50000);
        };
    }
}