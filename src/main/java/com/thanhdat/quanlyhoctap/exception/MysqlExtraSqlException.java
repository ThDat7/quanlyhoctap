package com.thanhdat.quanlyhoctap.exception;

import com.thanhdat.quanlyhoctap.exception.code.ErrorCode;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class MysqlExtraSqlException implements ExtraSqlException {
    private static final String TEMPLATE_MESSAGE = "Duplicate entry '{entry}' for key '{table_name}.{column_constraint}'";

    @Override
    public String getDetailDuplicateMessage(String baseMessage) {
        String duplicateValue = extractDuplicateValue(baseMessage);
        String tableName = extractTableName(baseMessage);
        ErrorCode duplicateErr = ErrorCode.DATABASE_DUPLICATE_ENTRY;
        return duplicateErr.getBaseMessage().replace("{value}", duplicateValue)
                .replace("{table}", tableName);
    }

    private String extractDuplicateValue(String errorMessage) {
        String[] parts = errorMessage.split("'");
        if (parts.length >= 2) {
            return parts[1];
        }
        return "";
    }

    private String extractTableName(String errorMessage) {
        String[] parts = errorMessage.split(" for key '");
        if (parts.length >= 2) {
            String keyPart = parts[1];
            return keyPart.split("\\.")[0];
        }
        return "";
    }
}
