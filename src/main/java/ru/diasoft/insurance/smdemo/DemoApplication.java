package ru.diasoft.insurance.smdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.statemachine.StateMachine;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

    private final StateMachine<States, Events> stateMachine;

    @Autowired
    public DemoApplication(StateMachine<States, Events> stateMachine) {
        this.stateMachine = stateMachine;
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void run(String... args) {
        MasterContext masterContext = MasterContext.getInstance();

        stateMachine.sendEvent(Events.ENTITY);
        masterContext.addParameter(Parameter.ENTITY_NAME,  "E1");
        stateMachine.sendEvent(Events.EN_MAIN);
        masterContext.addParameter(Parameter.ENTITY_IS_SYSTEM,  true);
        stateMachine.sendEvent(Events.EN_EXT);
        masterContext.addParameter(Parameter.ENTITY_COMMENT,  "E1 comment");
        stateMachine.sendEvent(Events.EN_COMMENT);
        stateMachine.sendEvent(Events.ENTITY);
        stateMachine.sendEvent(Events.START);

        stateMachine.sendEvent(Events.TABLE);
        masterContext.addParameter(Parameter.TABLE_NAME, "Т1");
        masterContext.addParameter(Parameter.TABLE_TYPE, Result.TableType.MAIN);
        stateMachine.sendEvent(Events.T_MAIN);
        stateMachine.sendEvent(Events.TABLE);
        stateMachine.sendEvent(Events.START);

        stateMachine.sendEvent(Events.TABLE);
        masterContext.addParameter(Parameter.TABLE_NAME, "Т2");
        masterContext.addParameter(Parameter.TABLE_TYPE, Result.TableType.HISTORICAL);
        stateMachine.sendEvent(Events.T_MAIN);
        stateMachine.sendEvent(Events.TABLE);
        stateMachine.sendEvent(Events.START);

        stateMachine.sendEvent(Events.ATTRIBUTE);
        masterContext.addParameter(Parameter.ATTRIBUTE_NAME,  "A1");
        masterContext.addParameter(Parameter.ATTRIBUTE_TYPE,  Result.AttributeType.FIELD);
        masterContext.addParameter(Parameter.ATTRIBUTE_KIND, Result.AttributeKind.FROM_DB);
        masterContext.addParameter(Parameter.ATTRIBUTE_LHV, true);
        stateMachine.sendEvent(Events.A_MAIN);
        stateMachine.sendEvent(Events.A_EXT);
        masterContext.addParameter(Parameter.ATTRIBUTE_FIELD, "attribute_field_1");
        stateMachine.sendEvent(Events.A_PLACE);
//        stateMachine.sendEvent(Events.A_HISTORY);
        stateMachine.sendEvent(Events.A_COMMENT);
        stateMachine.sendEvent(Events.ATTRIBUTE);
        stateMachine.sendEvent(Events.START);

        stateMachine.sendEvent(Events.ATTRIBUTE);
        masterContext.addParameter(Parameter.ATTRIBUTE_NAME,  "A2");
        masterContext.addParameter(Parameter.ATTRIBUTE_TYPE,  Result.AttributeType.ARRAY);
        masterContext.addParameter(Parameter.ATTRIBUTE_KIND, Result.AttributeKind.FROM_DB);
        stateMachine.sendEvent(Events.A_MAIN);
        stateMachine.sendEvent(Events.A_EXT);
        masterContext.addParameter(Parameter.ATTRIBUTE_FIELD, "attribute_field_2");
        stateMachine.sendEvent(Events.A_PLACE);
//        stateMachine.sendEvent(Events.A_HISTORY);
        stateMachine.sendEvent(Events.A_COMMENT);
        stateMachine.sendEvent(Events.ATTRIBUTE);
        stateMachine.sendEvent(Events.START);

        System.out.println(masterContext.getResult());
    }
}
