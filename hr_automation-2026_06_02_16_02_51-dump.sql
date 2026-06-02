--
-- PostgreSQL database dump
--

\restrict 7O6x0L75tPn1ayXPyX0W3fOGZ4KBw2AJF3DiqdWDtrU9QMJPrZw2AEyOmGD7R56

-- Dumped from database version 18.0
-- Dumped by pg_dump version 18.0

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: departments; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.departments (
    department_id bigint NOT NULL,
    name character varying(255) NOT NULL
);


ALTER TABLE public.departments OWNER TO postgres;

--
-- Name: departments_department_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.departments_department_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.departments_department_id_seq OWNER TO postgres;

--
-- Name: departments_department_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.departments_department_id_seq OWNED BY public.departments.department_id;


--
-- Name: employees; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.employees (
    employee_id bigint NOT NULL,
    email character varying(255),
    first_name character varying(255) NOT NULL,
    hire_date date,
    last_name character varying(255) NOT NULL,
    middle_name character varying(255),
    phone character varying(255),
    department_id bigint,
    position_id bigint
);


ALTER TABLE public.employees OWNER TO postgres;

--
-- Name: employees_employee_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.employees_employee_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.employees_employee_id_seq OWNER TO postgres;

--
-- Name: employees_employee_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.employees_employee_id_seq OWNED BY public.employees.employee_id;


--
-- Name: equipment; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.equipment (
    equipment_id bigint NOT NULL,
    item_name character varying(255) NOT NULL,
    serial_number character varying(255),
    comment character varying(255),
    employee_id bigint
);


ALTER TABLE public.equipment OWNER TO postgres;

--
-- Name: equipment_equipment_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.equipment_equipment_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.equipment_equipment_id_seq OWNER TO postgres;

--
-- Name: equipment_equipment_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.equipment_equipment_id_seq OWNED BY public.equipment.equipment_id;


--
-- Name: hr_requests; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.hr_requests (
    request_id bigint NOT NULL,
    request_type character varying(255),
    status character varying(255),
    created_date date,
    employee_id bigint,
    comment character varying(500),
    description character varying(1000)
);


ALTER TABLE public.hr_requests OWNER TO postgres;

--
-- Name: hr_requests_request_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.hr_requests_request_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.hr_requests_request_id_seq OWNER TO postgres;

--
-- Name: hr_requests_request_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.hr_requests_request_id_seq OWNED BY public.hr_requests.request_id;


--
-- Name: positions; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.positions (
    position_id bigint NOT NULL,
    title character varying(255) NOT NULL
);


ALTER TABLE public.positions OWNER TO postgres;

--
-- Name: positions_position_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.positions_position_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.positions_position_id_seq OWNER TO postgres;

--
-- Name: positions_position_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.positions_position_id_seq OWNED BY public.positions.position_id;


--
-- Name: roles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.roles (
    id integer NOT NULL,
    name character varying(50) NOT NULL
);


ALTER TABLE public.roles OWNER TO postgres;

--
-- Name: roles_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.roles_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.roles_id_seq OWNER TO postgres;

--
-- Name: roles_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.roles_id_seq OWNED BY public.roles.id;


--
-- Name: user_invites; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_invites (
    id integer NOT NULL,
    email character varying(100) NOT NULL,
    role_id integer NOT NULL
);


ALTER TABLE public.user_invites OWNER TO postgres;

--
-- Name: user_invites_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.user_invites_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.user_invites_id_seq OWNER TO postgres;

--
-- Name: user_invites_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.user_invites_id_seq OWNED BY public.user_invites.id;


--
-- Name: user_roles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_roles (
    user_id integer NOT NULL,
    role_id integer NOT NULL
);


ALTER TABLE public.user_roles OWNER TO postgres;

--
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    id integer NOT NULL,
    email character varying(100) NOT NULL,
    password character varying(255) NOT NULL,
    first_name character varying(50) NOT NULL,
    last_name character varying(50) NOT NULL,
    avatar_path character varying(255)
);


ALTER TABLE public.users OWNER TO postgres;

--
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.users_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.users_id_seq OWNER TO postgres;

--
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;


--
-- Name: departments department_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.departments ALTER COLUMN department_id SET DEFAULT nextval('public.departments_department_id_seq'::regclass);


--
-- Name: employees employee_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.employees ALTER COLUMN employee_id SET DEFAULT nextval('public.employees_employee_id_seq'::regclass);


--
-- Name: equipment equipment_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.equipment ALTER COLUMN equipment_id SET DEFAULT nextval('public.equipment_equipment_id_seq'::regclass);


--
-- Name: hr_requests request_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_requests ALTER COLUMN request_id SET DEFAULT nextval('public.hr_requests_request_id_seq'::regclass);


--
-- Name: positions position_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.positions ALTER COLUMN position_id SET DEFAULT nextval('public.positions_position_id_seq'::regclass);


--
-- Name: roles id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roles ALTER COLUMN id SET DEFAULT nextval('public.roles_id_seq'::regclass);


--
-- Name: user_invites id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_invites ALTER COLUMN id SET DEFAULT nextval('public.user_invites_id_seq'::regclass);


--
-- Name: users id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);


--
-- Data for Name: departments; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.departments (department_id, name) FROM stdin;
1	IT Department
2	Human Resources
3	Машинний відділ
4	Агрономічний
99	Звільнені
5	Бухгалтерія
\.


--
-- Data for Name: employees; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.employees (employee_id, email, first_name, hire_date, last_name, middle_name, phone, department_id, position_id) FROM stdin;
1	ivan.petrenko@company.com	Іван	2025-01-15	Петренко		+380501234567	1	1
4	kitvasil112@company.com	Василь	2022-03-01	Кіт		+380977653122	2	3
6	brovar.v32112@company.com	Віталій	2026-05-27	Бровар	Костянтинович	+380975683254	4	5
7	dgan3221123@company.com	Жан	2026-05-28	Чак	Жупель	+380912345896	1	6
10	gedz6471@company.com	Анатолій	2026-05-28	Гедзь	Олексійович	+380734731254	4	7
5	simbal1231@company.com	Максим	2026-05-27	Цимбал	Юрійович	+380964523121	3	4
11	nahornii424@company.com	Сергій	2026-05-28	Нагорний 	Янович	+380974812456	4	7
12	tatarchuk32312@company.com	Василь	2026-05-28	Татарчук	Олександрович	+380634568734	4	5
14	effqefeqf@wegw.cwl	цпцкп	2026-05-28	цкпкп	цупауцп	+38092385328	99	1
15	wefewfewf@wvwe.cdf	уацуацуа	2026-05-28	куауацу	цуацуацуа	+3809463548713	99	1
13	mitichkin123234@company.com	Владислав	2026-05-28	Мітічкін	Федорович	+380631235673	99	1
2	olena.kovalenko@company.com	Олена	2024-05-20	Коваленко		+380679876543	99	1
17	anna.v1224124@company.com	Анна	2026-05-28	Юрченко 	Владиславівна 	+380961264309	5	8
3	chepur.andrii@company.com	Андрій	2023-02-01	Чепуренко		+380971577256	1	9
8	shinkarenko1231@company.com	Юрій	2026-05-28	Шинкаренко		+380982536784	4	10
19	romanchuk123@company.com	Владислав	2026-05-28	Романчук 	Миколайович	+380964732567	4	7
20	petichkin1242@company.com	Олексій	2026-05-28	Петічкін 	Олексійович	+380975637123	1	9
18	gaidaichuk1123@company.com	Михайло	2026-05-28	Гайдайчук		+380964823691	2	2
\.


--
-- Data for Name: equipment; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.equipment (equipment_id, item_name, serial_number, comment, employee_id) FROM stdin;
2	Телефон Samsung A52	001231212341		1
5	Ноутбук LENOVO	001123112323		1
6	Телефон Samsung A70	31212321341		6
7	Ноутбук ASUS	43411214214121		6
8	MacBook Air M1	123213122412		7
9	Iphone 15	2132323114123		7
10	ASUS TUF	012324212312		8
11	Samsung A70	02132412321		8
12	ASUS ZINBOOK	10412412434		10
13	Samsung A70	134134341		10
14	ASUS ZINBOOK	01341343241		3
15	Samsung A70	21421321424		11
16	Планшет Samsung G50	124243431		11
17	Телефон Samsung A70	213252525234		12
18	Планшет Samsung G50	21412434123		12
19	Ноутбук DELL	2312421412		17
20	Телефон Samsung A70	2421434134		17
22	Телефон Samsung A70	131434123412		19
24	Планшет Samsung G50	12323123		19
26	ASUS ZINBOOK	1434234		20
27	MacBook M4 512gb	02102421412321		18
\.


--
-- Data for Name: hr_requests; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.hr_requests (request_id, request_type, status, created_date, employee_id, comment, description) FROM stdin;
2	Зміна робочого графіку	Погоджено	2026-05-20	2	\N	\N
3	Запит на підвищення зп	Відхилено	2026-05-10	3	\N	\N
1	Відпустка (14 днів)	Погоджено	2026-05-25	1	\N	\N
4	Відпустка	ВІДХИЛЕНО	2026-05-27	1	Потребує послуг IT: Ні	\N
5	Відпустка	ЗАВЕРШЕНО	2026-05-27	4	Потребує послуг IT: Ні	\N
13	Відпустка	ЗАВЕРШЕНО	2026-05-27	5	Потребує послуг IT: Ні	Прошу надати мені відпустку на 10 днів
6	Прийом на роботу	ЗАВЕРШЕНО	2026-05-27	5	Потребує послуг IT: Так. Коментар: Видати новий телефон і корпоративну пошту	\N
7	Зміна посади / відділу	ЗАВЕРШЕНО	2026-05-27	4	Потребує послуг IT: Ні	Запит на кадрове переведення. Новий відділ: 'IT Department' (був 'Human Resources'). Нова посада: 'QA Engineer' (була 'QA Engineer').
8	Запит IT-ресурсів (Редагування)	ЗАВЕРШЕНО	2026-05-27	3	Потребує послуг IT: Так. Завдання: видати новий телефон	Дані профілю оновлено. Надіслано запит на додаткове IT-забезпечення. 
14	Прийом на роботу	ЗАВЕРШЕНО	2026-05-27	6	Потребує послуг IT: Так. Що потрібно: Видати:\r\nкорпоративну пошту,\r\nтелефон Samsung A70,\r\nноутбук ASUS.	\N
9	Запит IT-ресурсів (Редагування)	ЗАВЕРШЕНО	2026-05-27	4	Потребує послуг IT: Так. Завдання: Видати новий ноутбук ASUS	Дані профілю оновлено. Надіслано запит на додаткове IT-забезпечення. 
10	Запит IT-ресурсів (Редагування)	ЗАВЕРШЕНО	2026-05-27	1	Потребує послуг IT: Так. Завдання: Видати новий ноутбук LENOVO	Дані профілю оновлено. Надіслано запит на додаткове IT-забезпечення. 
20	Прийом на роботу	ЗАВЕРШЕНО	2026-05-28	10	Потребує послуг IT: Так. Що потрібно: Видати корпоративну пошту, телефон, ноутбук	\N
11	Лікарняний	ЗАВЕРШЕНО	2026-05-27	1	Потребує послуг IT: Ні	Лікарняний на 7 днів
15	Звільнення	ЗАВЕРШЕНО	2026-05-27	2	Потребує послуг IT: Так. Що потрібно: Вилучити всі ресурси	\N
28	Звільнення	ЗАВЕРШЕНО	2026-05-28	14	Потребує послуг IT: Ні	\N
16	Прийом на роботу	ЗАВЕРШЕНО	2026-05-28	7	Потребує послуг IT: Так. Що потрібно: Видати ноутбук, телефон, корпоративну пошту	\N
17	Підвищення ЗП / Посади	ЗАВЕРШЕНО	2026-05-28	1	Потребує послуг IT: Ні	Прошу підвищити мені заробітню плату на 10%
21	Зміна посади / відділу	ЗАВЕРШЕНО	2026-05-28	3	Потребує послуг IT: Так. Завдання: Видати новий ноутбук	Кадрове переведення. Новий відділ: 'IT Department' (був 'IT Department'). Нова посада: 'Python Developer' (була 'Java Developer'). 
18	Прийом на роботу	ЗАВЕРШЕНО	2026-05-28	8	Потребує послуг IT: Так. Що потрібно: Видати корпоративну пошту, телефон, ноутбук	\N
24	Прийом на роботу	ЗАВЕРШЕНО	2026-05-28	12	Потребує послуг IT: Так. Що потрібно: Видати корпоративну пошту, телефон, ноутбук	Керівник: Замість ноутбука видати планшет
22	Відпустка	ВІДХИЛЕНО	2026-05-28	4	Потребує послуг IT: Ні	Прошу надати мені відпустку на 10 днів.
23	Прийом на роботу	ЗАВЕРШЕНО	2026-05-28	11	Потребує послуг IT: Так. Що потрібно: Видати корпоративну пошту, телефон, ноутбук	Керівник: Замість ноутбука видати планшет
25	Відпустка	ВІДХИЛЕНО	2026-05-28	4	Потребує послуг IT: Ні	Прошу надати мені відпустку на 14 днів.
26	Прийом на роботу	ВІДХИЛЕНО	2026-05-28	13	Потребує послуг IT: Так. Що потрібно: Видати ншцупрхцурпх0цу	\N
27	Прийом на роботу	ВІДХИЛЕНО	2026-05-28	14	Потребує послуг IT: Так. Що потрібно: яварукпукп	\N
31	Звільнення	ЗАВЕРШЕНО	2026-05-28	13	Потребує послуг IT: Ні	\N
30	Звільнення	ЗАВЕРШЕНО	2026-05-28	15	Потребує послуг IT: Ні	\N
33	Відпустка	ЗАВЕРШЕНО	2026-05-28	7	Потребує послуг IT: Ні	Керівник: Дайте відпочити 10 днів\n\n5 днів з тебе вистачить.
34	Прийом на роботу	ЗАВЕРШЕНО	2026-05-28	17	Потребує послуг IT: Так. Що потрібно: Видати корпоративну пошту, телефон, ноутбук	Керівник: 
35	Зміна посади / відділу	ЗАВЕРШЕНО	2026-05-28	3	Потребує послуг IT: Так	Кадрове переведення. Новий відділ: 'IT Department' (був 'IT Department'). Нова посада: 'Python Developer' (була 'Java Developer'). 
36	Зміна посади / відділу	ЗАВЕРШЕНО	2026-05-28	3	Потребує послуг IT: Так	Кадрове переведення. Новий відділ: 'IT Department' (був 'IT Department'). Нова посада: 'Python Developer' (була 'Java Developer'). 
37	Зміна посади / відділу	ЗАВЕРШЕНО	2026-05-28	3	Потребує послуг IT: Так	Кадрове переведення. Новий відділ: 'IT Department' (був 'IT Department'). Нова посада: 'Нова посада: 'Python Developer'' (була 'Java Developer'). 
38	Зміна посади / відділу	ЗАВЕРШЕНО	2026-05-28	3	Потребує послуг IT: Так	Кадрове переведення. Новий відділ: 'IT Department' (був 'IT Department'). Нова посада: 'Python Developer' (була 'Java Developer'). Коментар: Нова посада
45	Прийом на роботу	ЗАВЕРШЕНО	2026-05-28	20	Потребує послуг IT: Так. Що потрібно: Видати корпоративну пошту, ноутбук	\N
39	Зміна посади / відділу	ЗАВЕРШЕНО	2026-05-28	3	Потребує послуг IT: Так	Кадрове переведення. Новий відділ: 'IT Department' (був 'IT Department'). Нова посада: 'Python Developer' (була 'Java Developer'). 
40	Зміна посади / відділу	ЗАВЕРШЕНО	2026-05-28	3	Потребує послуг IT: Так	Кадрове переведення. Новий відділ: 'IT Department' (був 'IT Department'). Нова посада: 'Python Developer' (була 'Java Developer'). 
41	Зміна посади / відділу	ЗАВЕРШЕНО	2026-05-28	8	Потребує послуг IT: Ні	Кадрове переведення. Новий відділ: 'Агрономічний' (був 'Агрономічний'). Нова посада: 'Старший Агроном' (була 'Агроном'). 
42	Зміна посади / відділу	ЗАВЕРШЕНО	2026-05-28	8	Потребує послуг IT: Ні	Кадрове переведення. Старий відділ був: 'Агрономічний', тепер Новий відділ: 'Агрономічний'. Стара посада була: 'Агроном', тепер Нова посада: 'Старший Агроном'
43	Прийом на роботу	ЗАВЕРШЕНО	2026-05-28	18	Потребує послуг IT: Так. Що потрібно: Видати корпоративну пошту, ноутбук	\N
44	Прийом на роботу	ЗАВЕРШЕНО	2026-05-28	19	Потребує послуг IT: Так. Що потрібно: Видати корпоративну пошту, телефон, планшет	\N
47	Відпустка	ЗАВЕРШЕНО	2026-06-02	1	Потребує послуг IT: Ні	Прошу надати відпустку на 10 днів
51	Запит IT-ресурсів (Редагування)	ЗАВЕРШЕНО	2026-06-02	18	Потребує послуг IT: Так. Завдання: Потрібно замінити ноутбук працівнику	[IT]: Дані профілю оновлено. Надіслано запит на додаткове IT-забезпечення.\n[MANAGER]: Видати самий потужний пристрій.
48	Відпустка	ЗАВЕРШЕНО	2026-06-02	7	Потребує послуг IT: Ні	[HR]: Прошу надати відпустку на 10 днів\nКерівник: [HR]: Прошу надати відпустку на 10 днів\nЗа документом підійти в кабінет.
49	Лікарняний	ЗАВЕРШЕНО	2026-06-02	18	Потребує послуг IT: Ні	[HR]: Прошу надати лікарняний на 10 днів\nКерівник: За справкою зайти в кабінет
50	Лікарняний	ЗАВЕРШЕНО	2026-06-02	3	Потребує послуг IT: Ні	[MANAGER]: Прошу надати лікарняний на 10 днів\n[MANAGER]: за довідкою підійти в кабінет
46	Відпустка	ЗАВЕРШЕНО	2026-05-29	17	Потребує послуг IT: Ні	Керівник: Прошу надати відпустку на 14 днів.
52	Відпустка	ЗАВЕРШЕНО	2026-06-02	5	Потребує послуг IT: Ні	[IT]: Прошу надати відпустку на 10 днів\n[MANAGER]: За документом зайти в кабінет.
\.


--
-- Data for Name: positions; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.positions (position_id, title) FROM stdin;
1	Java Developer
2	HR Manager
3	QA Engineer
4	Старший механік
5	Агроном
6	Системний адміністратор
7	Молодший агроном
8	Бугалтер
9	Python Developer
10	Старший Агроном
\.


--
-- Data for Name: roles; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.roles (id, name) FROM stdin;
1	ROLE_HR
2	ROLE_MANAGER
3	ROLE_IT
\.


--
-- Data for Name: user_invites; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.user_invites (id, email, role_id) FROM stdin;
\.


--
-- Data for Name: user_roles; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.user_roles (user_id, role_id) FROM stdin;
1	3
2	2
3	1
4	1
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (id, email, password, first_name, last_name, avatar_path) FROM stdin;
2	boss.portal@company.com	$2a$10$wZ3hcSAhimWThVgU76U9kuQZnY1Dw8eG7NbaIxhP6O6ImYSqkD3pW	Олексій	Пилипчук	/images/avatars/4f5c0d2f-a9d7-45a0-9ed6-e552a51257a8_channels4_profile.jpg
3	hr.portal@company.com	$2a$10$Sw0i4dBZTh0dvA9/Pg/tfuQ2UndfAiiWTecp2qd4M85Kywep99S6i	Марія	Бровар	/images/avatars/7d7f997a-072a-4f8f-b7f6-688c279019d6_portrait-happy-woman-with-digital-tablet_329181-11681.avif
1	it.portal@company.com	$2a$10$Su9YApEWovAmoCKmPXxy0OWzIwH26q80f737J6QSI.zRt7wy4r.Oy	Олександр 	Зганяйко	/images/avatars/2690c243-62c6-4615-b8c1-37263f5172c9_programmer.png
4	hr1.portal@company.com	$2a$10$W/QxDHeKBBZflt/zBsn4FuZWYDDdWivmBwy8BfdFjpzl.3DbRk42y	Анна	Любченко 	https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&q=80&w=200
\.


--
-- Name: departments_department_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.departments_department_id_seq', 4, true);


--
-- Name: employees_employee_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.employees_employee_id_seq', 20, true);


--
-- Name: equipment_equipment_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.equipment_equipment_id_seq', 27, true);


--
-- Name: hr_requests_request_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.hr_requests_request_id_seq', 52, true);


--
-- Name: positions_position_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.positions_position_id_seq', 10, true);


--
-- Name: roles_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.roles_id_seq', 3, true);


--
-- Name: user_invites_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.user_invites_id_seq', 4, true);


--
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.users_id_seq', 4, true);


--
-- Name: departments departments_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.departments
    ADD CONSTRAINT departments_pkey PRIMARY KEY (department_id);


--
-- Name: employees employees_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.employees
    ADD CONSTRAINT employees_pkey PRIMARY KEY (employee_id);


--
-- Name: equipment equipment_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.equipment
    ADD CONSTRAINT equipment_pkey PRIMARY KEY (equipment_id);


--
-- Name: hr_requests hr_requests_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_requests
    ADD CONSTRAINT hr_requests_pkey PRIMARY KEY (request_id);


--
-- Name: positions positions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.positions
    ADD CONSTRAINT positions_pkey PRIMARY KEY (position_id);


--
-- Name: roles roles_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_name_key UNIQUE (name);


--
-- Name: roles roles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (id);


--
-- Name: employees uk_j9xgmd0ya5jmus09o0b8pqrpb; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.employees
    ADD CONSTRAINT uk_j9xgmd0ya5jmus09o0b8pqrpb UNIQUE (email);


--
-- Name: user_invites user_invites_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_invites
    ADD CONSTRAINT user_invites_email_key UNIQUE (email);


--
-- Name: user_invites user_invites_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_invites
    ADD CONSTRAINT user_invites_pkey PRIMARY KEY (id);


--
-- Name: user_roles user_roles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT user_roles_pkey PRIMARY KEY (user_id, role_id);


--
-- Name: users users_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_email_key UNIQUE (email);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: equipment fk_equipment_employee; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.equipment
    ADD CONSTRAINT fk_equipment_employee FOREIGN KEY (employee_id) REFERENCES public.employees(employee_id) ON DELETE CASCADE;


--
-- Name: hr_requests fk_hr_requests_employee; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hr_requests
    ADD CONSTRAINT fk_hr_requests_employee FOREIGN KEY (employee_id) REFERENCES public.employees(employee_id) ON DELETE CASCADE;


--
-- Name: user_invites fk_invite_role; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_invites
    ADD CONSTRAINT fk_invite_role FOREIGN KEY (role_id) REFERENCES public.roles(id);


--
-- Name: user_roles fk_role; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES public.roles(id) ON DELETE CASCADE;


--
-- Name: user_roles fk_user; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: employees fkgy4qe3dnqrm3ktd76sxp7n4c2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.employees
    ADD CONSTRAINT fkgy4qe3dnqrm3ktd76sxp7n4c2 FOREIGN KEY (department_id) REFERENCES public.departments(department_id);


--
-- Name: employees fkngcpgx7fx5kednw3m7u0u8of3; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.employees
    ADD CONSTRAINT fkngcpgx7fx5kednw3m7u0u8of3 FOREIGN KEY (position_id) REFERENCES public.positions(position_id);


--
-- PostgreSQL database dump complete
--

\unrestrict 7O6x0L75tPn1ayXPyX0W3fOGZ4KBw2AJF3DiqdWDtrU9QMJPrZw2AEyOmGD7R56

