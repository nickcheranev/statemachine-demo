package ru.diasoft.insurance.smdemo;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
public class Result {

    private final Logger LOGGER = LoggerFactory.getLogger(Result.class);

    private String entityName;
    private boolean isSystem;
    private String entityComment;
    private Set<Table> tables = new HashSet<>();
    private Set<Attribute> attributes = new HashSet<>();

    public void addTable(Table table) {
        tables.add(table);
    }

    public Attribute getAttribute(String attrName) {
        if(attrName==null) {
            Attribute newAttr = new Attribute();
            attributes.add(newAttr);
            return newAttr;
        }

        return attributes.stream()
                .filter(a -> Objects.requireNonNull(a.attributeName).equals(attrName))
                .findFirst()
                .orElseThrow();
    }

    public enum TableType {
        MAIN, HISTORICAL
    }

    @Data
    @AllArgsConstructor
    public static class Table {
        private String tableName;
        private TableType tableType;
    }

    public enum AttributeType {
        FIELD, REF, U_REF, ARRAY
    }

    public enum AttributeKind {
        FROM_DB, SYSTEM, CALC
    }

    @Data
    public static class Attribute {
        private String attributeName;
        private AttributeType attributeType;
        private AttributeKind attributeKind;
        private String attributeTable;
        private String attributeField;
        private Boolean isLastHistValue;
    }
}
