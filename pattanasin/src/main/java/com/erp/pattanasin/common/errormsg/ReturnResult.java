package com.erp.pattanasin.common.errormsg;

public class ReturnResult {
	private int	resultCode;		// 0 = SUCCESS, 1+ = ERROR
	private String  returnMessage;	// explain the result in more details

	
	public ReturnResult() {
		super();
	}


	public ReturnResult(int resultCode, String returnMessage) {
		super();
		this.resultCode = resultCode;
		this.returnMessage = returnMessage;
	}
	
	
	public void setup(int resultCode, String returnMessage) {

		this.resultCode = resultCode;
		this.returnMessage = returnMessage;
	}


	public int getResultCode() {
		return resultCode;
	}


	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}


	public String getreturnMessage() {
		return returnMessage;
	}


	public void setreturnMessage(String returnMessage) {
		this.returnMessage = returnMessage;
	}

	

}
