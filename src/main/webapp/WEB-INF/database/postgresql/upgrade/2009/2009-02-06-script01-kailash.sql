ALTER TABLE project_issue_replies ADD solution_date TIMESTAMP(3);
UPDATE project_issue_replies 
SET solution_date = modified
WHERE solution = true;
