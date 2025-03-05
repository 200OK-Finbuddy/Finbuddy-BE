package com.http200ok.finbuddy.mydata.controller;

import com.http200ok.finbuddy.mydata.dto.MyDataDeletionResult;
import com.http200ok.finbuddy.mydata.dto.MyDataGenerationResult;
import com.http200ok.finbuddy.mydata.service.MyDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * MyData 더미 데이터 생성 및 관리 컨트롤러
 */
@RestController
@RequestMapping("/api/mydata")
@RequiredArgsConstructor
public class MyDataController {

    private final MyDataService myDataService;

    /**
     * 특정 회원의 MyData 더미 데이터를 생성합니다.
     *
     * @param memberId 데이터를 생성할 회원 ID
     * @return 데이터 생성 결과
     */
    @PostMapping("/generate/{memberId}")
    public ResponseEntity<MyDataGenerationResult> generateDataForMember(@PathVariable("memberId") Long memberId) {
        MyDataGenerationResult result = myDataService.generateDummyDataForMember(memberId);

        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * 특정 회원의 기존 MyData 더미 데이터를 삭제합니다.
     *
     * @param memberId 데이터를 삭제할 회원 ID
     * @return 데이터 삭제 결과
     */
    @DeleteMapping("/delete/{memberId}")
    public ResponseEntity<MyDataDeletionResult> deleteDataForMember(@PathVariable("memberId") Long memberId) {
        MyDataDeletionResult result = myDataService.deleteExistingDataForMember(memberId);

        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * 특정 회원의 MyData 더미 데이터를 갱신합니다. (기존 데이터 삭제 후 새로 생성)
     *
     * @param memberId 데이터를 갱신할 회원 ID
     * @return 데이터 생성 결과
     */
    @PutMapping("/regenerate/{memberId}")
    public ResponseEntity<MyDataGenerationResult> regenerateDataForMember(@PathVariable("memberId") Long memberId) {
        // 기존 데이터 삭제
        MyDataDeletionResult deletionResult = myDataService.deleteExistingDataForMember(memberId);

        if (!deletionResult.isSuccess()) {
            return ResponseEntity.badRequest().body(MyDataGenerationResult.createResult(
                    memberId,
                    null,
                    0,
                    0,
                    0,
                    0,
                    false,
                    "기존 데이터 삭제 실패: " + deletionResult.getMessage()
            ));
        }

        // 새 데이터 생성
        MyDataGenerationResult generationResult = myDataService.generateDummyDataForMember(memberId);

        if (generationResult.isSuccess()) {
            return ResponseEntity.ok(generationResult);
        } else {
            return ResponseEntity.badRequest().body(generationResult);
        }
    }
}