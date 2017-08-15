package com.os4.ecb.session;

/**
 *
 * @author yanyan
 */
public class SessionEvent extends java.util.EventObject {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Object data;
	private SessionRequest request;

    public SessionEvent(Object o)
    {
        super( o );
    }

    public SessionEvent(Object o, Object data)
    {
        super( o );
        this.data = data;
    }
    
    public SessionEvent(Object o, Object data, SessionRequest request)
    {
        super( o );
        this.data = data;
        this.request = request;
    }

    public Object getData() {
    	return data;
    }
    
    public SessionRequest getRequest(){
    	return request;
    }
}
