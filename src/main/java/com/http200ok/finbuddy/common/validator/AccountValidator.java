package com.http200ok.finbuddy.common.validator;

import com.http200ok.finbuddy.account.domain.Account;

public interface AccountValidator {
    // 계좌 ID와 멤버 ID를 검증하여 계좌 소유권을 확인
    Account validateAndGetAccount(Long accountId, Long memberId);
    // 은행명과 계좌번호 정합성 확인
    void validateBankAccountMatch(String bankName, String accountNumber);
    // 계좌 소유권과 은행-계좌 일치 여부를 함께 검증, 이체 시 사용하는 통합 검증 메소드
    void validateAccountForTransfer(Long accountId, Long memberId, String bankName, String accountNumber);
}
