package com.blockchain.rest;

import javax.ws.rs.Path;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
@Path("/employer")
public class EmployerResource {
	protected final Logger logger = Logger.getLogger(getClass());


}