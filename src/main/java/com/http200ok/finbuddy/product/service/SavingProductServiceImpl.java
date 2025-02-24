package com.http200ok.finbuddy.product.service;

import com.http200ok.finbuddy.bank.domain.Bank;
import com.http200ok.finbuddy.bank.repository.BankRepository;
import com.http200ok.finbuddy.product.domain.SavingProduct;
import com.http200ok.finbuddy.product.domain.SavingProductOption;
import com.http200ok.finbuddy.product.dto.SavingProductDto;
import com.http200ok.finbuddy.product.repository.SavingProductRepository;
import io.github.cdimascio.dotenv.Dotenv;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class SavingProductServiceImpl implements SavingProductService {
    private static final Logger logger = LoggerFactory.getLogger(SavingProductServiceImpl.class);

    private final SavingProductRepository savingProductRepository;
    private final BankRepository bankRepository;
    private final RestTemplate restTemplate;

    private static final Dotenv dotenv = Dotenv.load();
    private static final String API_URL = "http://finlife.fss.or.kr/finlifeapi/savingProductsSearch.json?auth="
            + dotenv.get("BANK_API_KEY") + "&topFinGrpNo=020000&pageNo=1";

    // 날짜 포맷터 선언
    private static final DateTimeFormatter LOCAL_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final DateTimeFormatter LOCAL_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

    @Autowired
    public SavingProductServiceImpl(SavingProductRepository savingProductRepository,
                                    BankRepository bankRepository) {
        this.savingProductRepository = savingProductRepository;
        this.bankRepository = bankRepository;
        this.restTemplate = new RestTemplate();
    }

    @Transactional(readOnly = true)
    public SavingProductDto getSavingProductById(Long productId) {
        SavingProduct savingProduct = savingProductRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("적금 상품을 찾을 수 없습니다. ID: " + productId));
        return new SavingProductDto(savingProduct);
    }

    @Transactional
    public void fetchAndSaveSavingProducts() {
        try {
            logger.info("Fetching saving products from API: {}", API_URL);

            // HTTP 요청 수행
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(API_URL, HttpMethod.GET, entity, String.class);

            // 307 TEMPORARY_REDIRECT 처리
            if (responseEntity.getStatusCode() == HttpStatus.TEMPORARY_REDIRECT) {
                String redirectedUrl = responseEntity.getHeaders().getLocation().toString();
                logger.info("Redirected URL: {}", redirectedUrl);

                // 새 URL로 다시 요청
                responseEntity = restTemplate.exchange(redirectedUrl, HttpMethod.GET, entity, String.class);
            }

            // 응답 검증
            if (!responseEntity.getStatusCode().is2xxSuccessful() || responseEntity.getBody() == null) {
                throw new RuntimeException("API 응답이 올바르지 않습니다: " + responseEntity.getStatusCode());
            }

            // JSON 파싱
            String response = responseEntity.getBody();
            JSONObject jsonObject = new JSONObject(response);

            JSONArray baseList = jsonObject.getJSONObject("result").getJSONArray("baseList");
            JSONArray optionList = jsonObject.getJSONObject("result").getJSONArray("optionList");

            for (int i = 0; i < baseList.length(); i++) {
                JSONObject obj = baseList.getJSONObject(i);
                String bankCode = obj.getString("fin_co_no");
                String productCode = obj.getString("fin_prdt_cd");
                String productName = obj.getString("fin_prdt_nm");

                // 1. 은행 조회 (없으면 추가)
                Bank bank = bankRepository.findByCode(bankCode)
                        .orElseGet(() -> {
                            Bank newBank = new Bank();
                            newBank.setCode(bankCode);
                            newBank.setName(obj.getString("kor_co_nm"));
                            return bankRepository.save(newBank);
                        });

                // 2. 중복 확인 후 저장
                if (savingProductRepository.findByNameAndBank(productName, bank).isEmpty()) {
                    SavingProduct savingProduct = SavingProduct.createProduct(
                            bank,
                            productCode,
                            productName,
                            obj.optString("join_way", null),
                            obj.optString("mtrt_int", null),
                            obj.optString("spcl_cnd", null),
                            obj.optString("join_deny", null),
                            obj.optString("join_member", null),
                            obj.optString("etc_note", null),
                            obj.optLong("max_limit", 0),
                            parseYearMonthToLocalDate(obj.optString("dcls_month", null)), // 공시 제출월 → LocalDate (YYYY-MM-01)
                            parseLocalDate(obj.optString("dcls_strt_day", null)), // 공시 시작일
                            parseLocalDate(obj.optString("dcls_end_day", null)), // 공시 종료일
                            parseLocalDateTime(obj.optString("fin_co_subm_day", null)) // 금융회사 제출일
                    );
                    savingProductRepository.save(savingProduct);

                    // 3. 옵션 데이터 저장
                    for (int j = 0; j < optionList.length(); j++) {
                        JSONObject optionObj = optionList.getJSONObject(j);
                        if (optionObj.getString("fin_prdt_cd").equals(productCode)) {
                            SavingProductOption option = SavingProductOption.createSavingProductOption(
                                    savingProduct,
                                    optionObj.optString("intr_rate_type", null),
                                    optionObj.optString("intr_rate_type_nm", null),
                                    optionObj.optInt("save_trm", 0),
                                    optionObj.optDouble("intr_rate", 0.0),
                                    optionObj.optDouble("intr_rate2", 0.0),
                                    optionObj.optString("rsrv_type", null), // 추가된 데이터
                                    optionObj.optString("rsrv_type_nm", null) // 추가된 데이터
                            );
                            savingProduct.addOption(option);
                        }
                    }
                }
            }

            logger.info("Saving products successfully fetched and saved.");
        } catch (Exception e) {
            logger.error("API 요청 중 예외 발생: {}", e.getMessage(), e);
        }
    }


    // 날짜 변환 함수 추가
    private LocalDate parseYearMonthToLocalDate(String dateStr) {
        return Optional.ofNullable(dateStr)
                .filter(s -> !s.isEmpty())
                .map(s -> LocalDate.parse(s + "01", LOCAL_DATE_FORMATTER)) // YYYYMM → YYYYMM01 → LocalDate 변환
                .orElse(null);
    }

    private LocalDate parseLocalDate(String dateStr) {
        return Optional.ofNullable(dateStr)
                .filter(s -> !s.isEmpty())
                .map(s -> LocalDate.parse(s, LOCAL_DATE_FORMATTER))
                .orElse(null);
    }

    private LocalDateTime parseLocalDateTime(String dateStr) {
        return Optional.ofNullable(dateStr)
                .filter(s -> !s.isEmpty())
                .map(s -> LocalDateTime.parse(s, LOCAL_DATE_TIME_FORMATTER))
                .orElse(null);
    }
}
