package com.openraildata;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.activemq.ActiveMQComponent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * National Rail Open Data - Darwin v16 Client Demonstrator
 * Copyright (C)2019-2020 OpenTrainTimes Ltd.
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

    @Value("${darwinv16.password}")
    private String password;

    @Value("${darwinv16.accountname}")
    private String accountName;

    @Value("${darwinv16.hostname}")
    private String hostname;

    @Value("${darwinv16.topic.status}")
    private String statusTopic;

    @Value("${darwinv16.topic.feed}")
    private String feedTopic;

    private final CamelContext camelContext;
    private final DarwinMessageHandler darwinMessageHandler;
    private String username;
    private final String clientId;


    public DarwinClientRouteBuilder(CamelContext camelContext,
                                    DarwinMessageHandler darwinMessageHandler,
                                    @Value("${darwinv16.username}") String username) throws UnknownHostException {
        this.camelContext = camelContext;
        this.darwinMessageHandler = darwinMessageHandler;
        this.username = username;
        this.clientId = username + "-" + InetAddress.getLocalHost().getCanonicalHostName();
    }

    @Override
    public void configure() {

        var amqComponent = new ActiveMQComponent();
        amqComponent.setUsername(username);
        amqComponent.setPassword(password);
        amqComponent.setClientId(clientId);

        var cf = new ActiveMQConnectionFactory();
        cf.setTrustedPackages(List.of("com.thalesgroup.rtti"));
        cf.setBrokerURL("tcp://" + hostname + ":61616?jms.watchTopicAdvisories=false");
        amqComponent.setConnectionFactory(cf);

        camelContext.addComponent("activemq", amqComponent);

        from("activemq:topic:" + feedTopic + "?clientId=" + this.clientId + "&durableSubscriptionName=" + username + "-sub")
                .id("darwin-v16")
                .description("Darwin Push Port data v16")
                .unmarshal()
                .gzipDeflater()
                .process(darwinMessageHandler);

    }

}

