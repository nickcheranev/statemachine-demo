package ru.diasoft.insurance.smdemo;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class MasterContext {

    private static  MasterContext instance;

    private final Result result = new Result();
    private final Map<Parameter, Object> parameters = new HashMap<>();

    public static synchronized MasterContext getInstance() {
        if (instance == null) {
            return instance = new MasterContext();
        }
        return instance;
    }

    public void addParameter(Parameter parameter, Object value) {
       parameters.put(parameter, value);
    }

    public Object getParameter(Parameter parameter) {
        return parameters.get(parameter);
    }
}
