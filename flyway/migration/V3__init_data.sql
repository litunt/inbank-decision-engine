insert into core.segment (segment_type) values ('1');

insert into core.loan_segmentation (segment_id, credit_modifier)
values ((select max(id) from core.segment), 100);

insert into core.user_loan_profile (id_code, loan_segmentation_id, has_debt)
values ('49002010976', (select max(id) from core.loan_segmentation), false);


insert into core.segment (segment_type) values ('2');

insert into core.loan_segmentation (segment_id, credit_modifier)
values ((select max(id) from core.segment), 300);

insert into core.user_loan_profile (id_code, loan_segmentation_id, has_debt)
values ('49002010987', (select max(id) from core.loan_segmentation), false);


insert into core.segment (segment_type) values ('3');

insert into core.loan_segmentation (segment_id, credit_modifier)
values ((select max(id) from core.segment), 1000);

insert into core.user_loan_profile (id_code, loan_segmentation_id, has_debt)
values ('49002010998', (select max(id) from core.loan_segmentation), false);


insert into core.user_loan_profile (id_code, loan_segmentation_id, has_debt)
values ('49002010965', null, true);

