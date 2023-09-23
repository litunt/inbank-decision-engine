CREATE TABLE core.segment
(
    id   bigserial  NOT NULL,
    segment_type varchar(3) NOT NULL,
    CONSTRAINT PK_segment PRIMARY KEY (id)
)
;

CREATE TABLE core.loan_segmentation
(
    id              bigserial NOT NULL,
    segment_id      bigint    NOT NULL,
    credit_modifier integer   NOT NULL,
    CONSTRAINT PK_loan_segmentation PRIMARY KEY (id),
    CONSTRAINT FK_loan_segmentation_segment FOREIGN KEY (segment_id)
        REFERENCES core.segment (id) ON DELETE CASCADE ON UPDATE NO ACTION
)
;

CREATE TABLE core.user_loan_profile
(
    id                   bigserial   NOT NULL,
    id_code              varchar(50) NOT NULL,
    loan_segmentation_id bigint,
    has_debt boolean     NOT NULL,
    CONSTRAINT PK_user_loan_profile PRIMARY KEY (id),
    CONSTRAINT FK_user_loan_profile_loan_segmentation FOREIGN KEY (loan_segmentation_id)
        REFERENCES core.loan_segmentation (id) ON DELETE CASCADE ON UPDATE NO ACTION
)
;
