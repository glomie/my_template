package com.temp.tool;

public enum SmsSendNode {
	//风控相关节点
	SXCSWQY("授信超时未签约"),
	JKCSWQYFX("借款超时未签约(智享)"),
	JKCSWQYFFX("借款超时未签约(非智享)"),
	SQHDXTZ("申请后短信通知"),
	TZDBRZHDBZB("通知担保人做好担保准备"),
	THYHBZL("退回用户补资料"),
	SXQYWCDDB("授信签约完成待担保"),
	JKQYWCFX("借款签约完成(智享)"),
	JKQYWCFFX("借款签约完成(非智享)"),
	KDQBSXDQTX("口袋钱包授信到期提醒"),
	SXSHTGDQY("授信审核通过待签约"),
	SXSHTGDDB("授信审核通过待担保"),
	SXSHTG("授信审核通过"),
	TESHTG("提额审核通过"),
	SXSHBTG("授信审核不通过"),
	TESHBTG("提额审核不通过"),
	JKSHTGDQYFX("借款审核通过待签约(智享)"),
	JKSHTGDQYFFX("借款审核通过待签约(非智享)"),
	JKSHBTG("借款审核不通过"),
	CREDITCLOSED("授信额度关闭"),
	CREDITUPGRADE("授信额度提升"),
	
	//用户相关节点
	guarantee_submit("担保人提交认证审核"),
	guarantee_modify("担保人需修改资料提醒"),
	AUDIT_REJECT_TO_LOAN("担保人审核不通过（发给借款人）"),
	AUDIT_REJECT_TO_GUARANTEE("担保人审核不通过（发给担保人）"),
	guarantee_to_sign("担保人担保签约提醒"),
	guarantee_sign_pass("担保签约成功"),
	guarantee_sign_reject("担保人拒绝签约"),
	
	//商户相关节点
	fed_shop_sms_buyerReceive("买家收货（给卖家的短信）"),
	fed_shop_sms_sellerAgreeRefund("卖家同意退货（给卖家的短信）"),
	fed_shop_sms_orderSuccess("下单成功"),
	fed_shop_sms_buyerRefund("买家退货"),
	fed_shop_sms_agreeRefundToBuyer("卖家同意退货（给买家的短信）"),
	fed_shop_sms_disagreeRefund("卖家拒绝退货（给买家的短信）"),
	fed_shop_sms_remindDelivery("催促商家发货（给卖家的短信）"),
	fed_shop_sms_userReceive("用户收货（给买家的短信)"),
	
	//资金服相关
	FUNDS_DEPOSIT("充值成功"),
	FUNDS_DEPOSIT_FAILURE("充值失败"),
	FUNDS_WITHDRAW("提现成功"),
	FUNDS_WITHDRAW_FAILURE("提现失败"),

	// 定时任务相关
	QUARTZ_3DAYS_AGO_REPAYDATE_FX("还款期的前3天的早上9点还款提醒(智享)"),
	QUARTZ_3DAYS_AGO_REPAYDATE("还款期的前3天的早上9点还款提醒(非智享)"),
	QUARTZ_REPAYDATE("还款日的当天的早上9点还款提醒"),
	QUARTZ_OVERDUE("本金逾期提醒，逾期后第1、2、3、4、5、6、7、14、21、28天的早上8点开始自动触发"),
	QUARTZ_QUARTZ_3DAYS_AGO_REPAYDATE_CREDIT("还款期的前3天的早上9点还款提醒(信用卡)"),
	QUARTZ_QUARTZ_BILLDAY_CREDIT("信用卡账单日早上9点自动触发(信用卡)"),
	QUARTZ_QUARTZ_REPAYDATE_CREDIT("还款日的当天的早上9点触发(信用卡)"),
	QUARTZ_OVERDUE_CREDIT("短信本金逾期提醒，逾期后第1、2、3、4、5、6、7、14、21、28天的早上8点开始自动触发(信用卡)"),
	QUARTZ_BAD_DEBT("本金逾期提醒，逾期后第31 、33、35、37、40天的早上8点开始自动触发"),
	QUARTZ_BAD_DEBT_CREDIT("本金逾期提醒，逾期后第31 、33、35、37、40天的早上8点开始自动触发(信用卡)"),
	
	//回调相关
	NOTIFY_INSTALLMENT_PREPAY_CREDIT("信用卡分期提前还款短信发送(信用卡)"),
	NOTIFY_INSTALLMENT_FINANCEREPAY_CREDIT("信用卡财务还款(已出账)"),
	NOTIFY_INSTALLMENT_AUTOPAY_CREDIT("信用卡自动还款(已出账)"),
	NOTIFY_INSTALLMENT_USERPAY_ISSUE_CREDIT("信用卡用户还款(已出账)"),
	NOTIFY_INSTALLMENT_USERPAY_UNISSUE_CREDIT("信用卡用户还款(未出账)"),
	
	//回调放款还款
	NOTIFY_SJSH_FK("放款短信(随借随还)"),
	NOTIFY_FX_FK("放款短信(智享)"),
	NOTIFY_FFX_FK("放款短信(非智享)"),
	NOTIFY_FFX2_FK("放款短信(非智享-到账提示)"),
	NOTIFY_QUARTZ_HK("自动还款短信"),
	NOTIFY_FX_HK("手动还款短信(智享)"),
	NOTIFY_OTHER_HK("手动还款短信(非智享)"),
	NOTIFY_TQ_HK("提前还款短信"),

	//代扣
	WITHHOLD_AUTO_FAILURE("自动代扣失败"),
    WITHHOLD_MANU_FAILURE("手工代扣失败");
	
	private String value;
	private SmsSendNode(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
}

