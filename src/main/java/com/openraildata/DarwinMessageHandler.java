package com.openraildata;

import com.thalesgroup.rtti.pushport.v16.Pport;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.StringReader;

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
public class DarwinMessageHandler implements Processor {

    private static final Logger logger = LoggerFactory.getLogger(DarwinMessageHandler.class);

    private final JAXBContext jaxbContext = JAXBContext.newInstance(Pport.class);
    private final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

    public DarwinMessageHandler() throws JAXBException {
        // No-arguments constructor used to declare a JAXBException can be thrown
    }

    @Override
    public void process(Exchange exchange) throws Exception {

        var messageBody = exchange.getIn().getBody(String.class);

        var msgString = new StringReader(messageBody);
        var obj = (Pport) unmarshaller.unmarshal(msgString);

        logger.info("Received a Push Port message from {}", obj.getTs());
        logger.info("Raw XML is:\n{}", messageBody);

    }

}
