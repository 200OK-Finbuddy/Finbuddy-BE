package com.http200ok.finbuddy.product.service;

import com.http200ok.finbuddy.bank.domain.Bank;
import com.http200ok.finbuddy.bank.repository.BankRepository;
import com.http200ok.finbuddy.product.domain.DepositProduct;
import com.http200ok.finbuddy.product.repository.DepositProductRepository;
import io.github.cdimascio.dotenv.Dotenv;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
public class DepositProductServiceImpl implements DepositProductService {
    private final DepositProductRepository depositProductRepository;
    private final BankRepository bankRepository;
    private final RestTemplate restTemplate;

    private static final Dotenv dotenv = Dotenv.load();
    private static final String API_URL = "http://finlife.fss.or.kr/finlifeapi/depositProductsSearch.json?auth="
            + dotenv.get("BANK_API_KEY") + "&topFinGrpNo=020000&pageNo=1";

    public DepositProductServiceImpl(DepositProductRepository depositProductRepository, BankRepository bankRepository) {
        this.depositProductRepository = depositProductRepository;
        this.bankRepository = bankRepository;
        this.restTemplate = new RestTemplate();
    }

    @Transactional
    public void fetchAndSaveDepositProducts() {
        String response = restTemplate.getForObject(API_URL, String.class);
        JSONObject jsonObject = new JSONObject(response);
        JSONArray baseList = jsonObject.getJSONObject("result").getJSONArray("baseList");

        for (int i = 0; i < baseList.length(); i++) {
            JSONObject obj = baseList.getJSONObject(i);
            String bankCode = obj.getString("fin_co_no");
            String productName = obj.getString("fin_prdt_nm");

            // 1. 은행 조회 (없으면 새로 추가)
            Bank bank = bankRepository.findByCode(bankCode)
                    .orElseGet(() -> {
                        Bank newBank = new Bank();
                        newBank.setCode(bankCode);
                        newBank.setName(obj.getString("kor_co_nm"));
                        return bankRepository.save(newBank);
                    });

            // 2. 중복 확인 후 저장
            if (depositProductRepository.findByNameAndBank(productName, bank).isEmpty()) {
                DepositProduct deposit = new DepositProduct();
                deposit.setBank(bank);
                deposit.setName(productName);
                deposit.setSubscriptionMethod(obj.optString("join_way", null));
                deposit.setMaturityInterestRate(obj.optString("mtrt_int", null));
                deposit.setSpecialCondition(obj.optString("spcl_cnd", null));
                deposit.setSubscriptionRestriction(obj.optString("join_deny", null));
                deposit.setSubscriptionTarget(obj.optString("join_member", null));
                deposit.setAdditionalNotes(obj.optString("etc_note", null));
                deposit.setMaximumLimit(obj.optLong("max_limit", 0));

                depositProductRepository.save(deposit);
            }
        }
    }
}
