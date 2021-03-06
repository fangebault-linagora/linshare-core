SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET client_min_messages = warning;
SET default_with_oids = false;

CREATE SEQUENCE hibernate_sequence INCREMENT BY 1 NO MINVALUE NO MAXVALUE START WITH 1 CACHE 1;
CREATE TABLE account (
  id                               int8 NOT NULL, 
  domain_id                       int8 NOT NULL, 
  technical_account_permission_id int8, 
  owner_id                        int8, 
  ls_uuid                         varchar(255) NOT NULL, 
  creation_date                   timestamp(6) NOT NULL, 
  modification_date               timestamp(6) NOT NULL, 
  role_id                         int4 NOT NULL, 
  locale                          varchar(255) NOT NULL, 
  external_mail_locale            varchar(255) NOT NULL, 
  enable                          bool NOT NULL, 
  account_type                    int4 NOT NULL, 
  password                        varchar(255), 
  destroyed                       bool NOT NULL, 
  CONSTRAINT account_pkey 
    PRIMARY KEY (id));
CREATE TABLE anonymous_share_entry (
  entry_id          int8 NOT NULL, 
  downloaded        int8 NOT NULL, 
  document_entry_id int8 NOT NULL, 
  anonymous_url_id  int8 NOT NULL, 
  PRIMARY KEY (entry_id));
CREATE TABLE cookie (
  cookie_id   int8 NOT NULL, 
  identifier varchar(255) NOT NULL, 
  user_name  varchar(255) NOT NULL, 
  value      varchar(255) NOT NULL, 
  last_use   timestamp(6) NOT NULL, 
  CONSTRAINT linshare_cookie_pkey 
    PRIMARY KEY (cookie_id));
CREATE TABLE document (
  id               int8 NOT NULL, 
  uuid            varchar(255) NOT NULL UNIQUE, 
  creation_date   timestamp(6) NOT NULL, 
  type            varchar(255) NOT NULL, 
  size            int8 NOT NULL, 
  thmb_uuid       varchar(255), 
  timestamp       bytea, 
  check_mime_type bool DEFAULT 'false' NOT NULL, 
  CONSTRAINT linshare_document_pkey 
    PRIMARY KEY (id));
CREATE TABLE document_entry (
  entry_id    int8 NOT NULL, 
  document_id int8 NOT NULL, 
  ciphered    bool NOT NULL, 
  PRIMARY KEY (entry_id), 
  CONSTRAINT "unique document entry" 
    UNIQUE (entry_id, document_id));
CREATE TABLE domain_abstract (
  id                         int8 NOT NULL, 
  mime_policy_id            int8, 
  mailconfig_id             int8, 
  type                      int4 NOT NULL, 
  identifier                varchar(255) NOT NULL, 
  label                     varchar(255) NOT NULL, 
  enable                    bool NOT NULL, 
  template                  bool NOT NULL, 
  description               text NOT NULL, 
  default_role              int4 NOT NULL, 
  default_locale            varchar(255), 
  used_space                int8 NOT NULL, 
  user_provider_id          int8, 
  domain_policy_id          int8 NOT NULL, 
  parent_id                 int8, 
  messages_configuration_id int8 NOT NULL, 
  auth_show_order           int8 NOT NULL, 
  CONSTRAINT linshare_domain_abstract_pkey 
    PRIMARY KEY (id));
CREATE TABLE domain_access_policy (
  id  int8 NOT NULL, 
  CONSTRAINT linshare_domain_access_policy_pkey 
    PRIMARY KEY (id));
CREATE TABLE domain_access_rule (
  id                       int8 NOT NULL, 
  domain_access_rule_type int4 NOT NULL, 
  regexp                  varchar(255), 
  domain_id               int8, 
  domain_access_policy_id int8 NOT NULL, 
  rule_index              int4, 
  CONSTRAINT linshare_domain_access_rule_pkey 
    PRIMARY KEY (id));
CREATE TABLE domain_pattern (
  domain_pattern_id                             int8 NOT NULL, 
  identifier                                   varchar(255) NOT NULL, 
  description                                  text NOT NULL, 
  auth_command                                 text NOT NULL, 
  search_user_command                          text NOT NULL, 
  system                                       bool NOT NULL, 
  auto_complete_command_on_first_and_last_name text NOT NULL, 
  auto_complete_command_on_all_attributes      text NOT NULL, 
  search_page_size                             int4 NOT NULL, 
  search_size_limit                            int4 NOT NULL, 
  completion_page_size                         int4 NOT NULL, 
  completion_size_limit                        int4 NOT NULL, 
  CONSTRAINT linshare_domain_pattern_pkey 
    PRIMARY KEY (domain_pattern_id));
CREATE TABLE domain_policy (
  id                       int8 NOT NULL, 
  description             text, 
  identifier              varchar(255), 
  domain_access_policy_id int8, 
  CONSTRAINT linshare_domain_policy_pkey 
    PRIMARY KEY (id));
CREATE TABLE entry (
  id                 int8 NOT NULL, 
  owner_id          int8 NOT NULL, 
  creation_date     timestamp(6) NOT NULL, 
  modification_date timestamp(6) NOT NULL, 
  name              varchar(255) NOT NULL, 
  comment           text NOT NULL, 
  expiration_date   timestamp(6), 
  uuid              varchar(255) NOT NULL UNIQUE, 
  PRIMARY KEY (id));
CREATE TABLE functionality (
  id                       int8 NOT NULL, 
  system                  bool NOT NULL, 
  identifier              varchar(255) NOT NULL, 
  policy_activation_id    int8 NOT NULL, 
  policy_configuration_id int8 NOT NULL, 
  policy_delegation_id    int8, 
  domain_id               int8 NOT NULL, 
  param                   bool DEFAULT 'false' NOT NULL, 
  parent_identifier       varchar(255), 
  CONSTRAINT linshare_functionality_pkey 
    PRIMARY KEY (id));
CREATE TABLE functionality_integer (
  functionality_id int8 NOT NULL, 
  integer_value    int4, 
  CONSTRAINT linshare_functionality_integer_pkey 
    PRIMARY KEY (functionality_id));
CREATE TABLE functionality_string (
  functionality_id int8 NOT NULL, 
  string_value     varchar(255), 
  CONSTRAINT linshare_functionality_string_pkey 
    PRIMARY KEY (functionality_id));
CREATE TABLE functionality_unit (
  functionality_id int8 NOT NULL, 
  integer_value    int4, 
  unit_id          int8, 
  CONSTRAINT linshare_functionality_unit_pkey 
    PRIMARY KEY (functionality_id));
CREATE TABLE functionality_unit_boolean (
  functionality_id int8 NOT NULL, 
  integer_value    int4 NOT NULL, 
  boolean_value    bool NOT NULL, 
  unit_id          int8 NOT NULL, 
  CONSTRAINT linshare_functionality_unit_boolean_pkey 
    PRIMARY KEY (functionality_id));
CREATE TABLE ldap_connection (
  ldap_connection_id    int8 NOT NULL, 
  identifier           varchar(255) NOT NULL, 
  provider_url         varchar(255) NOT NULL, 
  security_auth        varchar(255), 
  security_principal   varchar(255), 
  security_credentials varchar(255), 
  CONSTRAINT linshare_ldap_connection_pkey 
    PRIMARY KEY (ldap_connection_id));
CREATE TABLE log_entry (
  id                int8 NOT NULL, 
  entry_type       varchar(255) NOT NULL, 
  action_date      timestamp(6) NOT NULL, 
  actor_mail       varchar(255) NOT NULL, 
  actor_firstname  varchar(255) NOT NULL, 
  actor_lastname   varchar(255) NOT NULL, 
  actor_domain     varchar(255), 
  log_action       varchar(255) NOT NULL, 
  description      text, 
  file_name        varchar(255), 
  file_type        varchar(255), 
  file_size        int8, 
  target_mail      varchar(255), 
  target_firstname varchar(255), 
  target_lastname  varchar(255), 
  target_domain    varchar(255), 
  expiration_date  timestamp(6), 
  CONSTRAINT linshare_log_entry_pkey 
    PRIMARY KEY (id));
CREATE TABLE mail_subjects (
  messages_configuration_id int8 NOT NULL, 
  subject_id                int4 NOT NULL, 
  language_id               int4 NOT NULL, 
  content                   text);
CREATE TABLE mail_templates (
  messages_configuration_id int8 NOT NULL, 
  template_id               int4 NOT NULL, 
  language_id               int4 NOT NULL, 
  content_html              text, 
  content_txt               text);
CREATE TABLE messages_configuration (
  messages_configuration_id  int8 NOT NULL, 
  CONSTRAINT linshare_messages_configuration_pkey 
    PRIMARY KEY (messages_configuration_id));
CREATE TABLE policy (
  id              int8 NOT NULL, 
  status         bool NOT NULL, 
  default_status bool NOT NULL, 
  policy         int4 NOT NULL, 
  system         bool NOT NULL, 
  CONSTRAINT linshare_policy_pkey 
    PRIMARY KEY (id));
CREATE TABLE recipient_favourite (
  id              int8 NOT NULL, 
  recipient_mail varchar(255) NOT NULL, 
  weight         int8 NOT NULL, 
  user_id        int8 NOT NULL, 
  CONSTRAINT linshare_recipient_favourite_pkey 
    PRIMARY KEY (id));
CREATE TABLE anonymous_url (
  id          int8 NOT NULL, 
  url_path   varchar(255) NOT NULL, 
  uuid       varchar(255) NOT NULL UNIQUE, 
  password   varchar(255), 
  contact_id int8 NOT NULL, 
  CONSTRAINT linshare_secured_url_pkey 
    PRIMARY KEY (id));
CREATE TABLE share_entry (
  entry_id          int8 NOT NULL, 
  document_entry_id int8 NOT NULL, 
  downloaded        int8 NOT NULL, 
  recipient_id      int8 NOT NULL, 
  PRIMARY KEY (entry_id));
CREATE TABLE share_expiry_rules (
  domain_id       int8 NOT NULL, 
  expiry_time     int4, 
  time_unit_id    int4, 
  share_size      int4, 
  size_unit_id    int4, 
  rule_sort_order int4 NOT NULL, 
  CONSTRAINT linshare_share_expiry_rules_pkey 
    PRIMARY KEY (domain_id, 
  rule_sort_order));
CREATE TABLE signature (
  id                 int8 NOT NULL, 
  owner_id          int8, 
  document_id       int8 NOT NULL, 
  uuid              varchar(255) NOT NULL UNIQUE, 
  name              varchar(255) NOT NULL, 
  creation_date     timestamp(6) NOT NULL, 
  modification_date timestamp(6) NOT NULL, 
  type              varchar(255), 
  size              int8, 
  cert_subject_dn   varchar(255), 
  cert_issuer_dn    varchar(255), 
  cert_not_after    timestamp(6), 
  cert              text, 
  sort_order        int4, 
  CONSTRAINT linshare_signature_pkey 
    PRIMARY KEY (id));
CREATE TABLE ldap_attribute (
  id                 int8 NOT NULL, 
  domain_pattern_id int8, 
  field             varchar(255) NOT NULL, 
  attribute         varchar(255) NOT NULL, 
  sync              bool NOT NULL, 
  system            bool NOT NULL, 
  enable            bool NOT NULL, 
  completion        bool NOT NULL, 
  PRIMARY KEY (id));
CREATE TABLE thread (
  account_id int8 NOT NULL, 
  name       varchar(255) NOT NULL, 
  PRIMARY KEY (account_id));
CREATE TABLE thread_entry (
  entry_id    int8 NOT NULL, 
  document_id int8 NOT NULL, 
  ciphered    bool NOT NULL, 
  PRIMARY KEY (entry_id), 
  CONSTRAINT "unique thread entry" 
    UNIQUE (entry_id, document_id));
CREATE TABLE thread_member (
  id                 int8 NOT NULL, 
  thread_id         int8 NOT NULL, 
  admin             bool NOT NULL, 
  can_upload        bool NOT NULL, 
  creation_date     timestamp(6) NOT NULL, 
  modification_date timestamp(6) NOT NULL, 
  user_id           int8 NOT NULL, 
  PRIMARY KEY (id));
CREATE TABLE unit (
  id          int8 NOT NULL, 
  unit_type  int4 NOT NULL, 
  unit_value int4 NOT NULL, 
  CONSTRAINT linshare_unit_pkey 
    PRIMARY KEY (id));
CREATE TABLE users (
  account_id            int8 NOT NULL, 
  first_name            varchar(255), 
  last_name             varchar(255), 
  mail                  varchar(255), 
  encipherment_key_pass bytea, 
  not_after             timestamp(6), 
  not_before            timestamp(6), 
  can_upload            bool NOT NULL, 
  comment               text, 
  restricted            bool NOT NULL, 
  expiration_date       timestamp, 
  ldap_uid              varchar(255), 
  can_create_guest      bool NOT NULL, 
  CONSTRAINT user_pkey 
    PRIMARY KEY (account_id));
CREATE TABLE user_provider_ldap (
  id                  int8 NOT NULL, 
  differential_key   varchar(255) NOT NULL, 
  domain_pattern_id  int8 NOT NULL, 
  ldap_connection_id int8 NOT NULL, 
  CONSTRAINT linshare_user_provider_ldap_pkey 
    PRIMARY KEY (id));
CREATE TABLE version (
  id       int8 NOT NULL, 
  version text NOT NULL UNIQUE, 
  CONSTRAINT linshare_version_pkey 
    PRIMARY KEY (id));
CREATE TABLE welcome_texts (
  messages_configuration_id int8 NOT NULL, 
  welcome_text              text, 
  language_id               int4);
CREATE TABLE allowed_contact (
  id          int8 NOT NULL, 
  account_id int8 NOT NULL, 
  contact_id int8 NOT NULL, 
  PRIMARY KEY (id));
CREATE TABLE technical_account_permission_domain_abstract (
  technical_account_permission_id int8 NOT NULL, 
  domain_abstract_id              int8 NOT NULL, 
  PRIMARY KEY (technical_account_permission_id, 
  domain_abstract_id));
CREATE TABLE technical_account_permission (
  id               int8 NOT NULL, 
  write           bool NOT NULL, 
  all_permissions bool NOT NULL, 
  PRIMARY KEY (id));
CREATE TABLE contact (
  id    int8 NOT NULL, 
  mail varchar(255) NOT NULL UNIQUE, 
  PRIMARY KEY (id));
CREATE TABLE technical_account_permission_account (
  technical_account_permission_id int8 NOT NULL, 
  account_id                      int8 NOT NULL, 
  PRIMARY KEY (technical_account_permission_id, 
  account_id));
CREATE TABLE mail_notification (
  id                       int8 NOT NULL, 
  configuration_policy_id int8 NOT NULL, 
  domain_abstract_id      int8 NOT NULL, 
  activation_policy_id    int8 NOT NULL, 
  identifier              varchar(255) NOT NULL, 
  system                  bool NOT NULL, 
  creation_date           timestamp(6) NOT NULL, 
  modification_date       timestamp(6) NOT NULL, 
  uuid                    varchar(255) NOT NULL, 
  PRIMARY KEY (id));
CREATE TABLE mail_config (
  id                   int8 NOT NULL, 
  mail_layout_html_id int8 NOT NULL, 
  domain_abstract_id  int8 NOT NULL, 
  name                varchar(255) NOT NULL, 
  visible             bool NOT NULL, 
  mail_layout_text_id int8 NOT NULL, 
  uuid                varchar(255) NOT NULL, 
  creation_date       timestamp(6) NOT NULL, 
  modification_date   timestamp(6) NOT NULL, 
  PRIMARY KEY (id));
CREATE TABLE mail_layout (
  id                  int8 NOT NULL, 
  domain_abstract_id int8 NOT NULL, 
  name               varchar(255) NOT NULL, 
  visible            bool NOT NULL, 
  layout             text NOT NULL, 
  creation_date      timestamp(6) NOT NULL, 
  modification_date  timestamp(6) NOT NULL, 
  uuid               varchar(255) NOT NULL, 
  plaintext          bool NOT NULL, 
  PRIMARY KEY (id));
CREATE TABLE mail_footer (
  id                  int8 NOT NULL, 
  domain_abstract_id int8 NOT NULL, 
  name               varchar(255) NOT NULL, 
  visible            bool NOT NULL, 
  language           int4 NOT NULL, 
  footer             text NOT NULL, 
  creation_date      timestamp(6) NOT NULL, 
  modification_date  timestamp(6) NOT NULL, 
  uuid               varchar(255) NOT NULL, 
  plaintext          bool NOT NULL, 
  PRIMARY KEY (id));
CREATE TABLE mail_footer_lang (
  id              int8 NOT NULL, 
  mail_config_id int8 NOT NULL, 
  mail_footer_id int8 NOT NULL, 
  language       int4 NOT NULL, 
  uuid           varchar(255) NOT NULL, 
  PRIMARY KEY (id));
CREATE TABLE mail_content (
  id                  int8 NOT NULL, 
  domain_abstract_id int8 NOT NULL, 
  name               varchar(255) NOT NULL, 
  visible            bool NOT NULL, 
  mail_content_type  int4 NOT NULL, 
  language           int4 NOT NULL, 
  subject            text NOT NULL, 
  greetings          text NOT NULL, 
  body               text NOT NULL, 
  uuid               varchar(255) NOT NULL, 
  plaintext          bool NOT NULL, 
  creation_date      timestamp(6) NOT NULL, 
  modification_date  timestamp(6) NOT NULL, 
  PRIMARY KEY (id));
CREATE TABLE mail_content_lang (
  id                 int8 NOT NULL, 
  language          int4 NOT NULL, 
  mail_content_id   int8 NOT NULL, 
  mail_config_id    int8 NOT NULL, 
  mail_content_type int4 NOT NULL, 
  uuid              varchar(255) NOT NULL, 
  PRIMARY KEY (id));
CREATE TABLE functionality_boolean (
  id                SERIAL NOT NULL, 
  functionality_id int8 NOT NULL, 
  boolean_value    bool NOT NULL, 
  PRIMARY KEY (id));
CREATE TABLE mailing_list (
  id                  int8 NOT NULL, 
  domain_abstract_id int8 NOT NULL, 
  user_id            int8 NOT NULL, 
  is_public          bool NOT NULL, 
  identifier         varchar(255) NOT NULL, 
  description        text, 
  uuid               varchar(255) NOT NULL, 
  creation_date      timestamp(6) NOT NULL, 
  modification_date  timestamp(6) NOT NULL, 
  PRIMARY KEY (id));
CREATE TABLE mailing_list_contact (
  id                          int8 NOT NULL, 
  mailing_list_id            int8 NOT NULL, 
  mail                       varchar(255) NOT NULL, 
  first_name                 varchar(255), 
  last_name                  varchar(255), 
  uuid                       varchar(255) NOT NULL, 
  creation_date              timestamp(6) NOT NULL, 
  modification_date          timestamp(6) NOT NULL, 
  mailing_list_contact_index int4 NOT NULL, 
  PRIMARY KEY (id));
CREATE TABLE mime_policy (
  id                 int8 NOT NULL, 
  domain_id         int8 NOT NULL, 
  uuid              varchar(255) NOT NULL, 
  name              varchar(255) NOT NULL, 
  mode              int4 NOT NULL, 
  displayable       int4 NOT NULL, 
  creation_date     timestamp(6) NOT NULL, 
  modification_date timestamp(6) NOT NULL, 
  PRIMARY KEY (id));
CREATE TABLE mime_type (
  id                 int8 NOT NULL, 
  mime_policy_id    int8 NOT NULL, 
  uuid              varchar(255) NOT NULL, 
  mime_type         text NOT NULL, 
  extensions        text NOT NULL, 
  enable            bool NOT NULL, 
  displayable       bool NOT NULL, 
  creation_date     timestamp(6) NOT NULL, 
  modification_date timestamp(6) NOT NULL, 
  PRIMARY KEY (id));
ALTER TABLE domain_abstract ADD CONSTRAINT fk449bc2ec4e302e7 FOREIGN KEY (user_provider_id) REFERENCES user_provider_ldap (id) ON UPDATE No action ON DELETE No action;
ALTER TABLE domain_abstract ADD CONSTRAINT fk449bc2ec59e1e332 FOREIGN KEY (domain_policy_id) REFERENCES domain_policy (id) ON UPDATE No action ON DELETE No action;
ALTER TABLE domain_abstract ADD CONSTRAINT fk449bc2ec9083e725 FOREIGN KEY (parent_id) REFERENCES domain_abstract (id) ON UPDATE No action ON DELETE No action;
ALTER TABLE domain_abstract ADD CONSTRAINT fk449bc2ec126ff4f2 FOREIGN KEY (messages_configuration_id) REFERENCES messages_configuration (messages_configuration_id) ON UPDATE No action ON DELETE No action;
ALTER TABLE domain_access_rule ADD CONSTRAINT fkf75719ed3c036ccb FOREIGN KEY (domain_id) REFERENCES domain_abstract (id) ON UPDATE No action ON DELETE No action;
ALTER TABLE domain_access_rule ADD CONSTRAINT fkf75719ed85924e31 FOREIGN KEY (domain_access_policy_id) REFERENCES domain_access_policy (id) ON UPDATE No action ON DELETE No action;
ALTER TABLE domain_policy ADD CONSTRAINT fk49c9a27c85924e31 FOREIGN KEY (domain_access_policy_id) REFERENCES domain_access_policy (id) ON UPDATE No action ON DELETE No action;
ALTER TABLE functionality ADD CONSTRAINT fk7430c53a58fe5398 FOREIGN KEY (policy_activation_id) REFERENCES policy (id) ON UPDATE No action ON DELETE No action;
ALTER TABLE functionality ADD CONSTRAINT fk7430c53a71796372 FOREIGN KEY (policy_configuration_id) REFERENCES policy (id) ON UPDATE No action ON DELETE No action;
ALTER TABLE functionality ADD CONSTRAINT fk7430c53a3c036ccb FOREIGN KEY (domain_id) REFERENCES domain_abstract (id) ON UPDATE No action ON DELETE No action;
ALTER TABLE functionality_integer ADD CONSTRAINT fk8662133910439d2b FOREIGN KEY (functionality_id) REFERENCES functionality (id) ON UPDATE No action ON DELETE No action;
ALTER TABLE functionality_string ADD CONSTRAINT fkb2a122b610439d2b FOREIGN KEY (functionality_id) REFERENCES functionality (id) ON UPDATE No action ON DELETE No action;
ALTER TABLE functionality_unit ADD CONSTRAINT fk3ced016910439d2b FOREIGN KEY (functionality_id) REFERENCES functionality (id) ON UPDATE No action ON DELETE No action;
ALTER TABLE functionality_unit ADD CONSTRAINT fk3ced0169f329e0c9 FOREIGN KEY (unit_id) REFERENCES unit (id) ON UPDATE No action ON DELETE No action;
ALTER TABLE functionality_unit_boolean ADD CONSTRAINT fk3ced016910439d2c FOREIGN KEY (functionality_id) REFERENCES functionality (id) ON UPDATE No action ON DELETE No action;
ALTER TABLE functionality_unit_boolean ADD CONSTRAINT fk3ced0169f329e0d9 FOREIGN KEY (unit_id) REFERENCES unit (id) ON UPDATE No action ON DELETE No action;
ALTER TABLE mail_subjects ADD CONSTRAINT fk1c97f3be126ff4f2 FOREIGN KEY (messages_configuration_id) REFERENCES messages_configuration (messages_configuration_id) ON UPDATE No action ON DELETE No action;
ALTER TABLE mail_templates ADD CONSTRAINT fkdd1b7f22126ff4f2 FOREIGN KEY (messages_configuration_id) REFERENCES messages_configuration (messages_configuration_id) ON UPDATE No action ON DELETE No action;
ALTER TABLE share_expiry_rules ADD CONSTRAINT fkfda1673c3c036ccb FOREIGN KEY (domain_id) REFERENCES domain_abstract (id) ON UPDATE No action ON DELETE No action;
ALTER TABLE signature ADD CONSTRAINT fk81c9a1a7c0bbd6f FOREIGN KEY (document_id) REFERENCES document (id) ON UPDATE No action ON DELETE No action;
ALTER TABLE user_provider_ldap ADD CONSTRAINT fk409cafb2372a0802 FOREIGN KEY (domain_pattern_id) REFERENCES domain_pattern (domain_pattern_id) ON UPDATE No action ON DELETE No action;
ALTER TABLE user_provider_ldap ADD CONSTRAINT fk409cafb23834018 FOREIGN KEY (ldap_connection_id) REFERENCES ldap_connection (ldap_connection_id) ON UPDATE No action ON DELETE No action;
ALTER TABLE welcome_texts ADD CONSTRAINT fk36a0c738126ff4f2 FOREIGN KEY (messages_configuration_id) REFERENCES messages_configuration (messages_configuration_id) ON UPDATE No action ON DELETE No action;
ALTER TABLE thread ADD CONSTRAINT inheritance_account_thread FOREIGN KEY (account_id) REFERENCES account (id);
ALTER TABLE document_entry ADD CONSTRAINT FKdocument_e594117 FOREIGN KEY (document_id) REFERENCES document (id);
ALTER TABLE share_entry ADD CONSTRAINT FKshare_entr708932 FOREIGN KEY (document_entry_id) REFERENCES document_entry (entry_id);
ALTER TABLE anonymous_share_entry ADD CONSTRAINT FKanonymous_138106 FOREIGN KEY (document_entry_id) REFERENCES document_entry (entry_id);
ALTER TABLE recipient_favourite ADD CONSTRAINT FKrecipient_90791 FOREIGN KEY (user_id) REFERENCES users (account_id);
ALTER TABLE anonymous_share_entry ADD CONSTRAINT FKanonymous_732508 FOREIGN KEY (anonymous_url_id) REFERENCES anonymous_url (id);
ALTER TABLE share_entry ADD CONSTRAINT FKshare_entr87036 FOREIGN KEY (recipient_id) REFERENCES account (id);
ALTER TABLE account ADD CONSTRAINT FKaccount400616 FOREIGN KEY (domain_id) REFERENCES domain_abstract (id);
ALTER TABLE ldap_attribute ADD CONSTRAINT FKldap_attri687153 FOREIGN KEY (domain_pattern_id) REFERENCES domain_pattern (domain_pattern_id);
ALTER TABLE allowed_contact ADD CONSTRAINT FKallowed_co409962 FOREIGN KEY (account_id) REFERENCES users (account_id);
ALTER TABLE allowed_contact ADD CONSTRAINT FKallowed_co620678 FOREIGN KEY (contact_id) REFERENCES users (account_id);
ALTER TABLE account ADD CONSTRAINT FKaccount487511 FOREIGN KEY (owner_id) REFERENCES account (id);
ALTER TABLE users ADD CONSTRAINT FKusers71760 FOREIGN KEY (account_id) REFERENCES account (id);
ALTER TABLE thread_member ADD CONSTRAINT FKthread_mem280144 FOREIGN KEY (thread_id) REFERENCES thread (account_id);
ALTER TABLE thread_member ADD CONSTRAINT FKthread_mem565048 FOREIGN KEY (user_id) REFERENCES users (account_id);
ALTER TABLE technical_account_permission_domain_abstract ADD CONSTRAINT FKtechnical_303831 FOREIGN KEY (technical_account_permission_id) REFERENCES technical_account_permission (id);
ALTER TABLE technical_account_permission_domain_abstract ADD CONSTRAINT FKtechnical_231219 FOREIGN KEY (domain_abstract_id) REFERENCES domain_abstract (id);
ALTER TABLE anonymous_share_entry ADD CONSTRAINT FKanonymous_621478 FOREIGN KEY (entry_id) REFERENCES entry (id);
ALTER TABLE document_entry ADD CONSTRAINT FKdocument_e19140 FOREIGN KEY (entry_id) REFERENCES entry (id);
ALTER TABLE share_entry ADD CONSTRAINT FKshare_entr50652 FOREIGN KEY (entry_id) REFERENCES entry (id);
ALTER TABLE thread_entry ADD CONSTRAINT FKthread_ent715634 FOREIGN KEY (entry_id) REFERENCES entry (id);
ALTER TABLE entry ADD CONSTRAINT FKentry500391 FOREIGN KEY (owner_id) REFERENCES account (id);
ALTER TABLE anonymous_url ADD CONSTRAINT FKanonymous_877695 FOREIGN KEY (contact_id) REFERENCES contact (id);
ALTER TABLE signature ADD CONSTRAINT FKsignature417918 FOREIGN KEY (owner_id) REFERENCES account (id);
ALTER TABLE technical_account_permission_account ADD CONSTRAINT FKtechnical_69967 FOREIGN KEY (technical_account_permission_id) REFERENCES technical_account_permission (id);
ALTER TABLE technical_account_permission_account ADD CONSTRAINT FKtechnical_622557 FOREIGN KEY (account_id) REFERENCES account (id);
ALTER TABLE account ADD CONSTRAINT FKaccount693567 FOREIGN KEY (technical_account_permission_id) REFERENCES technical_account_permission (id);
ALTER TABLE thread_entry ADD CONSTRAINT FKthread_ent140657 FOREIGN KEY (document_id) REFERENCES document (id);
ALTER TABLE mail_notification ADD CONSTRAINT FKmail_notif244118 FOREIGN KEY (activation_policy_id) REFERENCES policy (id);
ALTER TABLE mail_notification ADD CONSTRAINT FKmail_notif777760 FOREIGN KEY (domain_abstract_id) REFERENCES domain_abstract (id);
ALTER TABLE mail_layout ADD CONSTRAINT FKmail_layou627738 FOREIGN KEY (domain_abstract_id) REFERENCES domain_abstract (id);
ALTER TABLE mail_footer ADD CONSTRAINT FKmail_foote767112 FOREIGN KEY (domain_abstract_id) REFERENCES domain_abstract (id);
ALTER TABLE mail_footer_lang ADD CONSTRAINT FKmail_foote801249 FOREIGN KEY (mail_footer_id) REFERENCES mail_footer (id);
ALTER TABLE domain_abstract ADD CONSTRAINT FKdomain_abs160138 FOREIGN KEY (mailconfig_id) REFERENCES mail_config (id);
ALTER TABLE mail_config ADD CONSTRAINT FKmail_confi697783 FOREIGN KEY (domain_abstract_id) REFERENCES domain_abstract (id);
ALTER TABLE mail_footer_lang ADD CONSTRAINT FKmail_foote321102 FOREIGN KEY (mail_config_id) REFERENCES mail_config (id);
ALTER TABLE mail_content ADD CONSTRAINT FKmail_conte385227 FOREIGN KEY (domain_abstract_id) REFERENCES domain_abstract (id);
ALTER TABLE mail_content_lang ADD CONSTRAINT FKmail_conte910199 FOREIGN KEY (mail_config_id) REFERENCES mail_config (id);
ALTER TABLE mail_content_lang ADD CONSTRAINT FKmail_conte33952 FOREIGN KEY (mail_content_id) REFERENCES mail_content (id);
ALTER TABLE mail_config ADD CONSTRAINT FKmail_confi541299 FOREIGN KEY (mail_layout_html_id) REFERENCES mail_layout (id);
ALTER TABLE mail_config ADD CONSTRAINT FKmail_confi612314 FOREIGN KEY (mail_layout_text_id) REFERENCES mail_layout (id);
ALTER TABLE functionality ADD CONSTRAINT FKfunctional788903 FOREIGN KEY (policy_delegation_id) REFERENCES policy (id);
ALTER TABLE functionality_boolean ADD CONSTRAINT FKfunctional171577 FOREIGN KEY (functionality_id) REFERENCES functionality (id);
ALTER TABLE mailing_list ADD CONSTRAINT FKmailing_li478123 FOREIGN KEY (user_id) REFERENCES users (account_id);
ALTER TABLE mailing_list ADD CONSTRAINT FKmailing_li335663 FOREIGN KEY (domain_abstract_id) REFERENCES domain_abstract (id);
ALTER TABLE mailing_list_contact ADD CONSTRAINT FKMailingLis595962 FOREIGN KEY (mailing_list_id) REFERENCES mailing_list (id);
ALTER TABLE mail_notification ADD CONSTRAINT FKmail_notif791766 FOREIGN KEY (configuration_policy_id) REFERENCES policy (id);
ALTER TABLE mime_type ADD CONSTRAINT FKmime_type145742 FOREIGN KEY (mime_policy_id) REFERENCES mime_policy (id);
ALTER TABLE mime_policy ADD CONSTRAINT FKmime_polic613419 FOREIGN KEY (domain_id) REFERENCES domain_abstract (id);
ALTER TABLE domain_abstract ADD CONSTRAINT FKdomain_abs809928 FOREIGN KEY (mime_policy_id) REFERENCES mime_policy (id);
CREATE UNIQUE INDEX account_lsuid_index 
  ON account (ls_uuid);
CREATE UNIQUE INDEX account_ls_uuid 
  ON account (ls_uuid);
CREATE INDEX account_account_type 
  ON account (account_type);
CREATE INDEX cookie2 
  ON cookie (identifier);
CREATE INDEX cookie_i 
  ON cookie (cookie_id);
CREATE INDEX document_id_index 
  ON document (id);
CREATE INDEX document_i 
  ON document (uuid);
CREATE INDEX domain_abstract_id_index 
  ON domain_abstract (identifier);
CREATE UNIQUE INDEX domain_abstract_i 
  ON domain_abstract (user_provider_id);
CREATE INDEX domain_abstract_i2 
  ON domain_abstract (id);
CREATE INDEX domain_access_policy_index 
  ON domain_access_policy (id);
CREATE INDEX domain_access_rule_index 
  ON domain_access_rule (id);
CREATE INDEX domain_pattern_index 
  ON domain_pattern (identifier);
CREATE INDEX domain_pattern_i 
  ON domain_pattern (domain_pattern_id);
CREATE INDEX domain_policy_index 
  ON domain_policy (id);
CREATE UNIQUE INDEX domain_policy_i 
  ON domain_policy (domain_access_policy_id);
CREATE INDEX functionality_i 
  ON functionality (id);
CREATE UNIQUE INDEX functionality_index 
  ON functionality (policy_activation_id);
CREATE UNIQUE INDEX functionality_i2 
  ON functionality (identifier, domain_id);
CREATE UNIQUE INDEX functionality_i3 
  ON functionality (policy_configuration_id);
CREATE INDEX functionality_integer_index 
  ON functionality_integer (functionality_id);
CREATE INDEX functionality_string_index 
  ON functionality_string (functionality_id);
CREATE UNIQUE INDEX functionality_unit_index 
  ON functionality_unit (unit_id);
CREATE INDEX functionality_unit_i 
  ON functionality_unit (functionality_id);
CREATE UNIQUE INDEX functionality_unit_boolean_index 
  ON functionality_unit_boolean (unit_id);
CREATE INDEX functionality_unit_boolean_i 
  ON functionality_unit_boolean (functionality_id);
CREATE INDEX ldap_connection_index 
  ON ldap_connection (identifier);
CREATE INDEX ldap_connection_i 
  ON ldap_connection (ldap_connection_id);
CREATE INDEX log_entry_i 
  ON log_entry (actor_domain);
CREATE INDEX log_entry_i2 
  ON log_entry (file_name);
CREATE INDEX log_entry_i3 
  ON log_entry (actor_firstname);
CREATE INDEX log_entry_i4 
  ON log_entry (actor_mail);
CREATE INDEX log_entry_i5 
  ON log_entry (action_date);
CREATE INDEX log_entry_i6 
  ON log_entry (log_action);
CREATE INDEX log_entry_i7 
  ON log_entry (target_mail);
CREATE INDEX log_entry_i8 
  ON log_entry (actor_lastname);
CREATE INDEX log_entry_i9 
  ON log_entry (file_name);
CREATE INDEX log_entry_i10 
  ON log_entry (id);
CREATE INDEX policy_index 
  ON policy (id);
CREATE UNIQUE INDEX anonymous_url_i 
  ON anonymous_url (url_path, uuid);
CREATE INDEX anonymous_url_i2 
  ON anonymous_url (id);
CREATE INDEX share_expiry_rules_index 
  ON share_expiry_rules (domain_id);
CREATE INDEX signature_index 
  ON signature (id);
CREATE UNIQUE INDEX signature_i 
  ON signature (uuid);
CREATE INDEX unit_index 
  ON unit (id);
CREATE INDEX user_provider_ldap_index 
  ON user_provider_ldap (id);
CREATE INDEX welcome_texts_i 
  ON welcome_texts (messages_configuration_id);
CREATE INDEX mailing_list_index 
  ON mailing_list (uuid);
CREATE INDEX mailing_list_contact_index 
  ON mailing_list_contact (uuid);
