package com.openraildata;

import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * National Rail Open Data - Darwin v16 Client Demonstrator
 * Copyright (C)2019 OpenTrainTimes Ltd.
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
@Component
public class DarwinClientRouteBuilder extends RouteBuilder {

    @Value("${darwinv16.username}")
    private String username;

    @Value("${darwinv16.password}")
    private String password;

    @Value("${darwinv16.hostname}")
    private String hostname;

    @Value("${darwinv16.topic.status}")
    private String statusTopic;

    @Value("${darwinv16.topic.feed}")
    private String feedTopic;

    private final CamelContext camelContext;
    private final DarwinMessageHandler darwinMessageHandler;
    private final String clientId;


    public DarwinClientRouteBuilder(CamelContext camelContext,
                                    DarwinMessageHandler darwinMessageHandler) throws UnknownHostException {
        this.camelContext = camelContext;
        this.darwinMessageHandler = darwinMessageHandler;
        this.clientId = username + "-" + InetAddress.getLocalHost().getCanonicalHostName();
    }

    @Override
    public void configure() {

        ActiveMQComponent amqComponent = new ActiveMQComponent();
        amqComponent.setUsername(username);
        amqComponent.setPassword(password);
        amqComponent.setClientId(clientId);
        amqComponent.setBrokerURL("tcp://" + hostname + ":61616/?jms.watchTopicAdvisories=false");

        camelContext.addComponent("activemq", amqComponent);

        from("activemq:topic:" + feedTopic + "?durableSubscriptionName=" + username)
                .unmarshal()
                .gzip()
                .process(darwinMessageHandler);

    }

}
