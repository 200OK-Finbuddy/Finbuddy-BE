package com.http200ok.finbuddy.autotransfer.controller;

import com.http200ok.finbuddy.autotransfer.domain.AutoTransfer;
import com.http200ok.finbuddy.autotransfer.dto.AutoTransferCreateRequestDto;
import com.http200ok.finbuddy.autotransfer.dto.AutoTransferResponseDto;
import com.http200ok.finbuddy.autotransfer.service.AutoTransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/autotransfer")
@RequiredArgsConstructor
public class AutoTransferController {

    private final AutoTransferService autoTransferService;

    // 자동이체 생성
    @PostMapping
    public ResponseEntity<AutoTransferResponseDto> createAutoTransfer(
            @RequestBody AutoTransferCreateRequestDto request) {

        AutoTransfer autoTransfer = autoTransferService.createAutoTransfers(
                request.getFromAccountId(),
                request.getTargetAccountNumber(),
                request.getAmount(),
                request.getTransferDay()
        );

        return ResponseEntity.ok(new AutoTransferResponseDto(autoTransfer));
    }

}
