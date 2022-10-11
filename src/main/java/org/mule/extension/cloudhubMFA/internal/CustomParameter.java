package org.mule.extension.cloudhubMFA.internal;

import org.mule.runtime.api.meta.ExpressionSupport;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.Summary;
import org.mule.runtime.extension.api.annotation.values.OfValues;

public class CustomParameter {
 @Parameter
 @Expression(ExpressionSupport.SUPPORTED)
 @Summary("Enter the message in string format")
 private String Message;
 @Parameter
 @Expression(ExpressionSupport.SUPPORTED)
 @Summary("Enter the domain name")
 private String Domain;
 @Parameter
 @Expression(ExpressionSupport.SUPPORTED)
 @Optional
 @Summary("Enter the transaction Id")
 private String transactionId;
 @Parameter
 @OfValues(PriorityOptions.class)
 @Summary("Select the priority from drop-down")
 private String priority;
public String getMessage() {
	return Message;
}
public void setMessage(String message) {
	Message = message;
}
public String getDomain() {
	return Domain;
}
public void setDomain(String domain) {
	Domain = domain;
}
public String getTransactionId() {
	return transactionId;
}
public void setTransactionId(String transactionId) {
  String formattedtransactionId = (transactionId == null || transactionId.isEmpty())? "null" : transactionId;
	this.transactionId = formattedtransactionId;
}
public String getPriority() {
	return priority;
}
public void setPriority(String priority) {
	this.priority = priority;
}

}
