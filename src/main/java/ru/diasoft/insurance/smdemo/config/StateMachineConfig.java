package ru.diasoft.insurance.smdemo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.guard.Guard;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import ru.diasoft.insurance.smdemo.*;

import java.util.Arrays;
import java.util.HashSet;

import static ru.diasoft.insurance.smdemo.States.*;

/**
 * @author Cheranev N.
 * created on 04.11.2020.
 */
@Configuration
@EnableStateMachine
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<States, Events> {

    private static final Logger LOGGER = LoggerFactory.getLogger(StateMachineConfig.class);

    @Override
    public void configure(StateMachineConfigurationConfigurer<States, Events> config)
            throws Exception {
        config
                .withConfiguration()
                .autoStartup(true)
                .listener(listener())
        ;
    }

    @Override
    public void configure(StateMachineStateConfigurer<States, Events> states)
            throws Exception {
        states
            .withStates()
                .initial(START)
                .choice(A_PLACE)
                .end(END)
                .states(new HashSet<>(Arrays.asList(START, ENTITY, TABLE, ATTRIBUTE, METHOD, TYPE, STATUS, END)))
                .and()
                    .withStates()
                    .parent(START)
                    .initial(ENTITY)
                   // .end(ENTITY)
                    .states(new HashSet<>(Arrays.asList(ENTITY, EN_MAIN, EN_EXT, EN_COMMENT)))
                .and()
                    .withStates()
                    .parent(START)
                    .initial(TABLE)
                   // .end(ENTITY)
                    .states(new HashSet<>(Arrays.asList(TABLE, T_MAIN)))
                .and()
                    .withStates()
                    .parent(START)
                    .initial(ATTRIBUTE)
                   // .end(ENTITY)
                    .states(new HashSet<>(Arrays.asList(ATTRIBUTE, A_MAIN, A_EXT, A_PLACE, A_HISTORY, A_COMMENT)))
        ;
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<States, Events> transitions)
            throws Exception {
        transitions
            .withExternal()
                .source(START).target(ENTITY)
                .event(Events.ENTITY)
                .action(this::log)
                .and()
            .withExternal()
                .source(ENTITY).target(EN_MAIN)
                .event(Events.EN_MAIN)
                .action(this::addEntityMain)
                .and()
            .withExternal()
                .source(EN_MAIN).target(EN_EXT)
                .event(Events.EN_EXT)
                .action(this::addEntityExt)
                .and()
            .withExternal()
                .source(EN_EXT).target(EN_COMMENT)
                .event(Events.EN_COMMENT)
                .action(this::addEntityComment)
                .and()
            .withExternal()
                .source(EN_COMMENT).target(START)
                .event(Events.ENTITY)
                .action(this::log)
                .and()
            .withExternal()
                .source(ENTITY).target(START)
                .event(Events.START)
                .action(this::log)
                .and()

            .withExternal()
                .source(START).target(TABLE)
                .event(Events.TABLE)
                .action(this::log)
                .and()
            .withExternal()
                .source(TABLE).target(T_MAIN)
                .event(Events.T_MAIN)
                .action(this::addTableMain)
                .and()
            .withExternal()
                .source(T_MAIN).target(TABLE)
                .event(Events.TABLE)
                .action(this::log)
                .and()
            .withExternal()
                .source(TABLE).target(START)
                .event(Events.START)
                .action(this::log)
                .and()

            .withExternal()
                .source(START).target(ATTRIBUTE)
                .event(Events.ATTRIBUTE)
                .action(this::log)
                .and()
            .withExternal()
                .source(ATTRIBUTE).target(A_MAIN)
                .event(Events.A_MAIN)
                .action(this::addAttributeMain)
                .and()
            .withExternal()
                .source(A_MAIN).target(A_EXT)
                .event(Events.A_EXT)
                .action(this::log)
                .and()
            .withExternal()
                .source(A_EXT).target(A_PLACE)
                .event(Events.A_PLACE)
                .action(this::addAttributePlace)
                .and()
            .withChoice()
                .source(A_PLACE)
                .first(States.A_HISTORY, guardAttrHistory(), this::actionAttrHistory)
                .last(States.A_COMMENT)
                .and()
            .withExternal()
                .source(A_HISTORY).target(A_COMMENT)
                .event(Events.A_COMMENT)
                .action(this::log)
                .and()
            .withExternal()
                .source(A_COMMENT).target(ATTRIBUTE)
                .event(Events.ATTRIBUTE)
                .action(this::log)
                .and()
            .withExternal()
                .source(ATTRIBUTE).target(START)
                .event(Events.START)
                .action(this::log)
                .and()
        ;
    }

    private Guard<States, Events> guardAttrHistory() {
        return context -> {
            MasterContext masterContext = MasterContext.getInstance();
            return  !masterContext.getParameter(Parameter.ATTRIBUTE_TYPE).equals(Result.AttributeType.ARRAY);
        };
    }

    private void addEntityMain(StateContext<States, Events> context) {
        log(context);
        MasterContext masterContext = MasterContext.getInstance();
        String name = (String) masterContext.getParameter(Parameter.ENTITY_NAME);
        masterContext.getResult().setEntityName(name);
    }

    private void addEntityExt(StateContext<States, Events> context) {
        log(context);
        MasterContext masterContext = MasterContext.getInstance();
        Boolean isSystem = (Boolean) masterContext.getParameter(Parameter.ENTITY_IS_SYSTEM);
        masterContext.getResult().setSystem(isSystem);
    }

    private void addEntityComment(StateContext<States, Events> context) {
        log(context);
        MasterContext masterContext = MasterContext.getInstance();
        String comment = (String) masterContext.getParameter(Parameter.ENTITY_COMMENT);
        masterContext.getResult().setEntityComment(comment);
    }

    private void addTableMain(StateContext<States, Events> context) {
        log(context);
        MasterContext masterContext = MasterContext.getInstance();
        String tableName = (String) masterContext.getParameter(Parameter.TABLE_NAME);
        Result.TableType tableType = (Result.TableType) masterContext.getParameter(Parameter.TABLE_TYPE);
        masterContext.getResult().addTable(new Result.Table(tableName, tableType));
    }

    private void addAttributeMain(StateContext<States, Events> context) {
        log(context);
        MasterContext masterContext = MasterContext.getInstance();
        Result result = masterContext.getResult();
        Result.Attribute attr = result.getAttribute(null);
        attr.setAttributeName((String) masterContext.getParameter(Parameter.ATTRIBUTE_NAME));
        attr.setAttributeType((Result.AttributeType) masterContext.getParameter(Parameter.ATTRIBUTE_TYPE));
        attr.setAttributeKind((Result.AttributeKind) masterContext.getParameter(Parameter.ATTRIBUTE_KIND));
    }

    private void addAttributePlace(StateContext<States, Events> context) {
        log(context);
        MasterContext masterContext = MasterContext.getInstance();
        Result result = masterContext.getResult();
        String attrName = (String) masterContext.getParameter(Parameter.ATTRIBUTE_NAME);
        Result.Attribute attr = result.getAttribute(attrName);
        attr.setAttributeTable((String) masterContext.getParameter(Parameter.TABLE_NAME));
        attr.setAttributeField((String) masterContext.getParameter(Parameter.ATTRIBUTE_FIELD));
    }

    private void actionAttrHistory(StateContext<States, Events> context) {
        MasterContext masterContext = MasterContext.getInstance();
        String attrName = (String) masterContext.getParameter(Parameter.ATTRIBUTE_NAME);
        Result.Attribute attr = masterContext.getResult().getAttribute(attrName);
        boolean attrLhv = (boolean) masterContext.getParameter(Parameter.ATTRIBUTE_LHV);
        attr.setIsLastHistValue(attrLhv);
    }

    private void log(StateContext<States, Events> context) {
        LOGGER.info("Action {}", context.getEvent());
    }

    @Bean
    public StateMachineListener<States, Events> listener() {
        return new StateMachineListenerAdapter<>() {
            @Override
            public void stateChanged(State<States, Events> from, State<States, Events> to) {
                LOGGER.info("State change to {}", to.getId());
            }
        };
    }

    @Bean
    public Guard<States, Events> guard() {
        return context -> false;
    }
}
