CREATE TABLE "test_example_id"(
    "id" BIGINT NOT NULL,
    "input" VARCHAR(255) NOT NULL,
    "output" VARCHAR(255) NOT NULL,
    "discription" VARCHAR(255) NOT NULL
);
ALTER TABLE
    "test_example_id" ADD PRIMARY KEY("id");
CREATE TABLE "submissions"(
    "id" BIGINT NOT NULL,
    "user_id" BIGINT NOT NULL,
    "problem_id" BIGINT NOT NULL,
    "file_name" VARCHAR(255) NOT NULL,
    "result" VARCHAR(255) NOT NULL,
    "status" VARCHAR(255) NOT NULL
);
ALTER TABLE
    "submissions" ADD PRIMARY KEY("id");
CREATE TABLE "testcases"(
    "id" BIGINT NOT NULL,
    "input" VARCHAR(255) NOT NULL,
    "output" VARCHAR(255) NOT NULL
);
ALTER TABLE
    "testcases" ADD PRIMARY KEY("id");
CREATE TABLE "categorys"(
    "id" BIGINT NOT NULL,
    "title" VARCHAR(255) NOT NULL
);
ALTER TABLE
    "categorys" ADD PRIMARY KEY("id");
CREATE TABLE "problems"(
    "id" BIGINT NOT NULL,
    "title" VARCHAR(255) NOT NULL,
    "category_id" BIGINT NOT NULL,
    "testcase_id" BIGINT NOT NULL,
    "body" VARCHAR(255) NOT NULL,
    "test_example_id" BIGINT NOT NULL,
    "point" FLOAT(53) NOT NULL,
    "number_accept" BIGINT NOT NULL,
    "time_limit" FLOAT(53) NOT NULL,
    "memory" BIGINT NOT NULL,
    "comment_id" BIGINT NOT NULL,
    "author_id" BIGINT NOT NULL,
    "created_at" DATE NOT NULL,
    "hidden" BOOLEAN NOT NULL
);
ALTER TABLE
    "problems" ADD PRIMARY KEY("id");
CREATE TABLE "contest_problems"(
    "id" BIGINT NOT NULL,
    "contest_id" BIGINT NOT NULL,
    "problem_id" BIGINT NOT NULL,
    "created_at" BIGINT NOT NULL
);
ALTER TABLE
    "contest_problems" ADD PRIMARY KEY("id");
CREATE TABLE "users"(
    "id" BIGINT NOT NULL,
    "username" VARCHAR(255) NOT NULL,
    "password" VARCHAR(255) NOT NULL,
    "fullname" VARCHAR(255) NOT NULL,
    "role" VARCHAR(255) NOT NULL
);
ALTER TABLE
    "users" ADD PRIMARY KEY("id");
CREATE TABLE "contests"(
    "id" BIGINT NOT NULL,
    "title" VARCHAR(255) NOT NULL,
    "author_id" BIGINT NOT NULL,
    "start_time" DATE NOT NULL,
    "end_time" DATE NOT NULL
);
ALTER TABLE
    "contests" ADD PRIMARY KEY("id");
ALTER TABLE
    "problems" ADD CONSTRAINT "problems_category_id_foreign" FOREIGN KEY("category_id") REFERENCES "categorys"("id");
ALTER TABLE
    "problems" ADD CONSTRAINT "problems_author_id_foreign" FOREIGN KEY("author_id") REFERENCES "users"("id");
ALTER TABLE
    "problems" ADD CONSTRAINT "problems_testcase_id_foreign" FOREIGN KEY("testcase_id") REFERENCES "testcases"("id");
ALTER TABLE
    "contest_problems" ADD CONSTRAINT "contest_problems_contest_id_foreign" FOREIGN KEY("contest_id") REFERENCES "contests"("id");
ALTER TABLE
    "submissions" ADD CONSTRAINT "submissions_user_id_foreign" FOREIGN KEY("user_id") REFERENCES "users"("id");
ALTER TABLE
    "contest_problems" ADD CONSTRAINT "contest_problems_problem_id_foreign" FOREIGN KEY("problem_id") REFERENCES "problems"("id");
ALTER TABLE
    "submissions" ADD CONSTRAINT "submissions_problem_id_foreign" FOREIGN KEY("problem_id") REFERENCES "problems"("id");
ALTER TABLE
    "problems" ADD CONSTRAINT "problems_test_example_id_foreign" FOREIGN KEY("test_example_id") REFERENCES "test_example_id"("id");