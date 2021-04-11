package ru.diasoft.insurance.smdemo;

public enum Parameter {
    ENTITY_NAME("entity.main.name"),
    ENTITY_IS_SYSTEM("entity.ext.system"),
    ENTITY_COMMENT("entity.comment"),
    TABLE_NAME("table.main.name"),
    TABLE_TYPE("table.main.type"),
    ATTRIBUTE_NAME("attribute.main.name"),
    ATTRIBUTE_TYPE("attribute.main.type"),
    ATTRIBUTE_KIND("attribute.main.kind"),
    ATTRIBUTE_FIELD("attribute.place.field"),
    ATTRIBUTE_LHV("attribute.history.lhv")
    ;

    private final String text;

    Parameter(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public Parameter byText(String text) {
        return valueOf(text);
    }

}
