package com.http200ok.finbuddy.product.service;

import com.http200ok.finbuddy.bank.domain.Bank;
import com.http200ok.finbuddy.bank.repository.BankRepository;
import com.http200ok.finbuddy.product.domain.DepositProduct;
import com.http200ok.finbuddy.product.domain.DepositProductOption;
import com.http200ok.finbuddy.product.repository.DepositProductRepository;
import io.github.cdimascio.dotenv.Dotenv;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import static org.json.XMLTokener.entity;

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
        try {
            // HTTP 요청 수행 (307 리디렉션 감지 가능)
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(API_URL, HttpMethod.GET, entity, String.class);

            // 307 TEMPORARY_REDIRECT 처리
            if (responseEntity.getStatusCode() == HttpStatus.TEMPORARY_REDIRECT) {
                String redirectedUrl = responseEntity.getHeaders().getLocation().toString();
                System.out.println("Redirected URL: " + redirectedUrl);

                // 새 URL로 다시 요청
                responseEntity = restTemplate.exchange(redirectedUrl, HttpMethod.GET, entity, String.class);
            }

            // 응답이 정상인지 확인
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
                    DepositProduct deposit = DepositProduct.createProduct(
                            bank,
                            productCode,
                            productName,
                            obj.optString("join_way", null),
                            obj.optString("mtrt_int", null),
                            obj.optString("spcl_cnd", null),
                            obj.optString("join_deny", null),
                            obj.optString("join_member", null),
                            obj.optString("etc_note", null),
                            obj.optLong("max_limit", 0)
                    );
                    depositProductRepository.save(deposit);

                    for (int j = 0; j < optionList.length(); j++) {
                        JSONObject optionObj = optionList.getJSONObject(j);
                        if (optionObj.getString("fin_prdt_cd").equals(obj.getString("fin_prdt_cd"))) {
                            DepositProductOption option = DepositProductOption.createDepositProductOption(
                                    deposit,
                                    optionObj.optString("intr_rate_type_nm", null),
                                    optionObj.optString("rsrv_type_nm", null),
                                    optionObj.optInt("save_trm", 0),
                                    optionObj.optDouble("intr_rate", 0.0),
                                    optionObj.optDouble("intr_rate2", 0.0)
                            );
                            deposit.addOption(option);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("API 요청 중 예외 발생: " + e.getMessage());
        }
    }
}
