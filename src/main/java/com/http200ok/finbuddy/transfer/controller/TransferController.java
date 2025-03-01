package com.http200ok.finbuddy.transfer.controller;

import com.http200ok.finbuddy.account.dto.ReceivingAccountResponseDto;
import com.http200ok.finbuddy.transfer.dto.TransferRequestDto;
import com.http200ok.finbuddy.transfer.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transfers")
public class TransferController {

    private final TransferService transferService;

    @GetMapping("/receiving-account")
    public ResponseEntity<ReceivingAccountResponseDto> getReceivingAccount(@RequestParam("bankName") String bankName, @RequestParam("accountNumber") String accountNumber) {
        ReceivingAccountResponseDto receivingAccount = transferService.getReceivingAccount(bankName, accountNumber);
        return ResponseEntity.ok(receivingAccount);
    }

    /**
     * 계좌 이체 API
     */
    @PostMapping
    public ResponseEntity<?> executeTransfer(@RequestBody TransferRequestDto transferRequestDto, @RequestParam("memberId") Long memberId) {
        boolean result = transferService.executeAccountTransfer(
                memberId,
                transferRequestDto.getFromAccountId(),
                transferRequestDto.getToBankName(),
                transferRequestDto.getToAccountNumber(),
                transferRequestDto.getAmount(),
                transferRequestDto.getPassword(),
                transferRequestDto.getSenderName(),
                transferRequestDto.getReceiverName()
        );

        // 이체 성공 후 예산 초과 확인 및 알림 전송
//        if (result) {
//            transferService.checkAndNotifyBudgetExceededOnTransaction(memberId);
//        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "이체가 성공적으로 완료되었습니다.");

        return ResponseEntity.ok(response);
    }

}
