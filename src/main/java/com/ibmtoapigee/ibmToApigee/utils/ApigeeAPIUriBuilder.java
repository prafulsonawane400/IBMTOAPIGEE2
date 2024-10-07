package com.ibmtoapigee.ibmToApigee.utils;

public class ApigeeAPIUriBuilder {

	static enum ApigeeAction {
		CREATE_PROXY, DEPLOY
	}

	public static String getURI(Enum<ApigeeAction> apigeeAction, String apigeeOrgURI, String organization, String env,
			String fileName, Boolean isApigeeX) {
		String output = null;
		if (apigeeAction == ApigeeAction.CREATE_PROXY) {
			output = apigeeOrgURI + "/" + organization + "/apis?name=" + fileName + "&action=import";
			if (isApigeeX) {
				output = apigeeOrgURI + "/organizations/" + organization + "/apis?name=" + fileName + "&action=import";
			}
			return output;
		} else if (apigeeAction == ApigeeAction.DEPLOY) {
			output = apigeeOrgURI + "/" + organization + "/environments/" + env + "/apis/" + fileName
					+ "/revisions/1/deployments";
			if (isApigeeX) {
				output = apigeeOrgURI + "/organizations/" + organization + "/environments/" + env + "/apis/" + fileName
						+ "/revisions/1/deployments";
			}
			return output;
		}
		throw new UnsupportedOperationException("Opertion not found!!!");
	}

}
