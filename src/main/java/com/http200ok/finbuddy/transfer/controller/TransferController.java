package com.http200ok.finbuddy.transfer.controller;

import com.http200ok.finbuddy.account.repository.AccountRepository;
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
    private final AccountRepository accountRepository;

    /**
     * 계좌 이체 API
     */
    @PostMapping
    public ResponseEntity<?> transferMoney(@RequestBody TransferRequestDto transferRequestDto, @RequestParam("memberId") Long memberId) {
        try {
            boolean result = transferService.transferMoney(
                    memberId,
                    transferRequestDto.getFromAccountNumber(),
                    transferRequestDto.getToAccountNumber(),
                    transferRequestDto.getAmount(),
                    transferRequestDto.getPassword(),
                    transferRequestDto.getSenderName(),
                    transferRequestDto.getReceiverName()
            );

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "이체가 성공적으로 완료되었습니다.");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * 계좌 유효성 확인 API
     */
    @GetMapping("/validate-account/{accountNumber}")
    public ResponseEntity<?> validateAccount(@PathVariable String accountNumber) {
        boolean exists = accountRepository.existsByAccountNumber(accountNumber);

        Map<String, Object> response = new HashMap<>();
        response.put("exists", exists);

        if (exists) {
            accountRepository.findByAccountNumber(accountNumber)
                    .ifPresent(account -> {
                        response.put("accountName", account.getAccountName());
                        response.put("bankName", account.getBank().getName());
                    });
        }

        return ResponseEntity.ok(response);
    }

}
