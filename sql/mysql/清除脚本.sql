#清除文件相关表数据
DELETE from infra_demo01_contact;
DELETE from infra_demo02_category;
DELETE from infra_demo03_course;
DELETE from infra_demo03_grade;
DELETE from infra_demo03_student;
DELETE from infra_file;
DELETE from infra_file_content;
DELETE from infra_codegen_table;
DELETE from infra_codegen_column;
DELETE from infra_api_access_log;
DELETE from infra_api_error_log;

#清除定时任务相关表数据
DELETE from infra_job_log;

#清除会员相关表数据
DELETE from member_action_log;
DELETE from member_address;
DELETE from member_article_visit;
DELETE from member_exchange_record;
DELETE from member_experience_record;
DELETE from member_group;
DELETE from member_level;
DELETE from member_level_record;
DELETE from member_message_remind;
DELETE from member_point_record;
DELETE from member_feedback;
DELETE from member_question_record;
DELETE from member_sign_in_config;
DELETE from member_sign_in_record;
DELETE from member_statistics;
DELETE from member_tag;
DELETE from member_visit_log;
DELETE from member_user;

#清除公众号相关表数据
DELETE from mp_account where id<>1;
UPDATE mp_account set `name`='微信小程序账号',app_id='',app_secret='',token='',expire_date=null,deleted=0  where id=1;
DELETE from mp_material;

#清除支付相关表数据
DELETE from pay_app;
DELETE from pay_channel;
DELETE from pay_demo_transfer;
DELETE from pay_notify_log;
DELETE from pay_notify_task;
DELETE from pay_order;
DELETE from pay_order_extension;
DELETE from pay_refund;
DELETE from pay_transfer;
DELETE from pay_wallet;
DELETE from pay_wallet_recharge;
DELETE from pay_wallet_recharge_package;

#清除商品相关表数据
DELETE from product_brand;
DELETE from product_category;
DELETE from product_property;
DELETE from product_comment;
DELETE from product_property_value;
DELETE from product_sku;
DELETE from product_spu;
DELETE from product_visit_log;

#清除文章相关表数据
DELETE from promotion_article;
DELETE from promotion_article_category;
DELETE from promotion_banner;

#清除促销相关表数据
DELETE from promotion_combination_activity;
DELETE from promotion_coupon;
DELETE from promotion_coupon_template;
DELETE from promotion_discount_activity;
DELETE from promotion_discount_product;
DELETE from promotion_diy_page;
DELETE from promotion_diy_template;
DELETE from promotion_reward_activity;
DELETE from promotion_seckill_activity;
DELETE from promotion_seckill_config;


#清除系统相关表数据
DELETE from system_dept;
DELETE from system_dict_data where dict_type not in (select type from system_dict_type);
DELETE from system_login_log;
DELETE from system_mail_account;
DELETE from system_mail_log;
DELETE from system_mail_template;
#DELETE from system_menu where deleted = 1;
DELETE from system_notice;
DELETE from system_notify_message;
DELETE from system_notify_template;
DELETE from system_oauth2_access_token;
DELETE from system_oauth2_approve;
DELETE from system_oauth2_client where id<>1;
UPDATE system_oauth2_client set name='管理系统' where id=1;
DELETE from system_oauth2_code;
DELETE from system_oauth2_refresh_token;
DELETE from system_operate_log;
DELETE from system_post;
DELETE from system_question;
DELETE from system_question_dimension;
DELETE from system_question_factor;
DELETE from system_question_topic;
DELETE from system_role where id not in (1,2);
DELETE from system_role_menu where role_id in (1,2);
DELETE from system_sensitive_word;
DELETE from system_sms_channel;
DELETE from system_sms_code;
DELETE from system_sms_log;
DELETE from system_sms_template;
DELETE from system_social_client;
DELETE from system_social_user;
DELETE from system_social_user_bind;
DELETE from system_tenant where id <>1;
UPDATE system_tenant set `name`='管理系统',contact_mobile=null,contact_name='',website=null where id =1;
DELETE from system_tenant_package;
DELETE from system_users where id <>1;
DELETE from system_user_role where user_id<>1;
DELETE from system_user_post where user_id <>1;
DELETE from exchange_config;
DELETE from exchange_config_item;


#清除交易订单相关表数据
DELETE from trade_after_sale;
DELETE from trade_after_sale_log;
DELETE from trade_brokerage_record;
DELETE from trade_brokerage_user;
DELETE from trade_brokerage_withdraw;
DELETE from trade_cart;
DELETE from trade_delivery_express;
DELETE from trade_delivery_express_template;
DELETE from trade_delivery_express_template_charge;
DELETE from trade_delivery_express_template_free;
DELETE from trade_delivery_pick_up_store;
DELETE from trade_delivery_pick_up_store_staff;
DELETE from trade_order;
DELETE from trade_order_item;
DELETE from trade_order_log;
DELETE from trade_statistics;