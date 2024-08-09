package com.bytepound.thstandapp.business.util;

import com.bytepound.thstandapp.business.constant.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.function.Supplier;

@Slf4j
@Service
public class DBUtils {

    private final TransactionTemplate transactionTemplate;

    public DBUtils(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }

    /**
     * 检查updateInvoker返回的int值是否是1，常用于单笔更新
     *
     * @param updateInvoker 更新语句
     * @return 实际影响的行数
     */
    public Integer checkAffectedRows(Supplier<Integer> updateInvoker) {
        return checkAffectedRows(updateInvoker, 1);
    }

    /**
     * 检查updateInvoker返回的int值是否是expectedRows，常用于多笔更新
     *
     * @param updateInvoker 更新语句
     * @param expectedRows 期望影响行数
     * @return 实际影响的行数
     */
    public Integer checkAffectedRows(Supplier<Integer> updateInvoker, int expectedRows) {
        return checkAffectedRows(updateInvoker, expectedRows, ErrorCode.SERVER_ERROR);
    }

    /**
     * 检查updateInvoker返回的int值是否是expectedRows，常用于多笔更新
     *
     * @param updateInvoker 更新语句
     * @param expectedRows 期望影响行数
     * @param bizErrorCode 自定义错误码
     * @return 实际影响的行数
     */
    public Integer checkAffectedRows(Supplier<Integer> updateInvoker, int expectedRows, ErrorCode bizErrorCode) {
        final Integer affectedRows = updateInvoker.get();
        if (affectedRows != expectedRows) {
            log.error("DB affect rows not:{}, actual:{}", expectedRows, affectedRows);
            throw bizErrorCode.buildBizException();
        }
        return affectedRows;
    }
}
