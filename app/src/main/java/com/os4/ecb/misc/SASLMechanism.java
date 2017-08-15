package com.os4.ecb.misc;

import com.os4.ecb.session.SessionUser;

public class SASLMechanism {

    private static final String INITAL_NONCE = "00000001";
    private static final String QOP_VALUE    = "auth";
    private static final String PROTOCOL     = "xmpp";
    private SessionUser sessionUser;

	public SASLMechanism(){}
	public SASLMechanism(SessionUser sessionUser){
		this.sessionUser = sessionUser;
	}

	public SessionUser getSessionUser() {
		return sessionUser;
	}

	public void setSessionUser(SessionUser sessionUser) {
		this.sessionUser = sessionUser;
	}

	public byte[] evaluateChallenge(byte[] challenge) throws Exception {
		
		String nonce = getKeyValue(challenge,"nonce");
		String cnonce = StringUtils.randomString(40);
		String digestUri = PROTOCOL + "/" + sessionUser.getDomain();

        byte[] A1 = concact(MD5.bytes(sessionUser.getUsername() + ':' + sessionUser.getDomain() + ':' + sessionUser.getPassword()), StringUtils.toBytes(':' + nonce + ':' + cnonce + ':' + sessionUser.getUsername()));
        String hex_hashed_a1 = StringUtils.encodeHex(MD5.bytes(A1));
        
		String responseValue = calcResponse(digestUri,hex_hashed_a1,nonce,cnonce);
		String saslString = createResponse(sessionUser.getUsername(),sessionUser.getDomain(),nonce,cnonce,digestUri,responseValue,sessionUser.getUsername());
		byte[] response = StringUtils.toBytes(saslString);	    
		
		return response;
	}

	private String getKeyValue(byte[] response,String keyRef) throws Exception {
		
		String challengeString = new String(response, "UTF-8");
		String[] challengeParts = challengeString.split(",");
		for (String part : challengeParts) {
			String[] keyValue = part.split("=");
			assert (keyValue.length == 2);
			String key = keyValue[0];
			key = key.replaceFirst("^\\s+", "");
			String value = keyValue[1];
			if (keyRef.equals(key)) {
				if(value.contains("\"")) return value.replace("\"", "");
				else return value;
			}
		}
		return null;
	}
	
	private String createResponse(String authenticationId,String serviceName,String nonce,String cnonce,String digestUri,String responseValue,String authorizationId){
		StringBuilder a1 = new StringBuilder();
		a1.append("charset=utf-8");
		a1.append(",username=\"" + authenticationId + '"');
		a1.append(",realm=\"" + serviceName + '"');
		a1.append(",nonce=\"" + nonce + '"');
		a1.append(",nc=00000001");
		a1.append(",cnonce=\"" + cnonce + '"');
		a1.append(",digest-uri=\"" + digestUri + '"');
		a1.append(",maxbuf=65536");
		a1.append(",response=" + responseValue);
		a1.append(",qop=auth");
		a1.append(",authzid=\"" + authorizationId + '"');
		return a1.toString();
	}
	
	private String calcResponse(String digestUri,String hex_hashed_a1,String nonce,String cnonce) {		
		StringBuilder A2 = new StringBuilder();
		A2.append("AUTHENTICATE");
		A2.append(':');
		A2.append(digestUri);
		String hex_hashed_a2 = StringUtils.encodeHex(MD5.bytes(A2.toString()));

		StringBuilder kd_argument = new StringBuilder();
		kd_argument.append(hex_hashed_a1);
		kd_argument.append(':');
		kd_argument.append(nonce);
		kd_argument.append(':');
		kd_argument.append(INITAL_NONCE);
		kd_argument.append(':');
		kd_argument.append(cnonce);
		kd_argument.append(':');
		kd_argument.append(QOP_VALUE);
		kd_argument.append(':');
		kd_argument.append(hex_hashed_a2);
		
		byte[] kd = MD5.bytes(kd_argument.toString());
		String responseValue = StringUtils.encodeHex(kd);
		return responseValue;
	}
	  
    private byte[] concact(byte[] arrayOne, byte[] arrayTwo) {
        int combinedLength = arrayOne.length + arrayTwo.length;
        byte[] res = new byte[combinedLength];
        System.arraycopy(arrayOne, 0, res, 0, arrayOne.length);
        System.arraycopy(arrayTwo, 0, res, arrayOne.length, arrayTwo.length);
        return res;
    }
}
