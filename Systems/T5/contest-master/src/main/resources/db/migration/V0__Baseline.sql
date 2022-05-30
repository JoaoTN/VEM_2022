CREATE TABLE public.arquivo (
    id integer NOT NULL,
    nome character varying(255),
    formato character varying(255)
);


--
-- TOC entry 199 (class 1259 OID 266275)
-- Name: arquivo_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.arquivo_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2219 (class 0 OID 0)
-- Dependencies: 199
-- Name: arquivo_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.arquivo_id_seq OWNED BY public.arquivo.id;


--
-- TOC entry 190 (class 1259 OID 249312)
-- Name: coautores_trabalho; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.coautores_trabalho (
    coautor_id bigint NOT NULL,
    trabalho_id bigint NOT NULL
);


--
-- TOC entry 174 (class 1259 OID 27475)
-- Name: evento; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.evento (
    id bigint NOT NULL,
    descricao character varying(255),
    estado character varying(255),
    nome character varying(255) NOT NULL,
    visibilidade character varying(255),
    termino_submissao date,
    prazo_notificacao date,
    inicio_submissao date,
    camera_ready date
);


--
-- TOC entry 175 (class 1259 OID 27481)
-- Name: evento_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.evento_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2220 (class 0 OID 0)
-- Dependencies: 175
-- Name: evento_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.evento_id_seq OWNED BY public.evento.id;


--
-- TOC entry 188 (class 1259 OID 73689)
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 201 (class 1259 OID 266326)
-- Name: modalidade; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.modalidade (
    id bigint NOT NULL,
    nome character varying(255) NOT NULL,
    evento_id bigint
);


--
-- TOC entry 202 (class 1259 OID 266337)
-- Name: modalidade_apresentacao_evento; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.modalidade_apresentacao_evento (
    evento_id bigint NOT NULL,
    modalidade_id bigint NOT NULL
);


--
-- TOC entry 200 (class 1259 OID 266324)
-- Name: modalidade_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.modalidade_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2221 (class 0 OID 0)
-- Dependencies: 200
-- Name: modalidade_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.modalidade_id_seq OWNED BY public.modalidade.id;


--
-- TOC entry 203 (class 1259 OID 266350)
-- Name: modalidade_submissao_evento; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.modalidade_submissao_evento (
    evento_id bigint NOT NULL,
    modalidade_id bigint NOT NULL
);


--
-- TOC entry 192 (class 1259 OID 249338)
-- Name: organizadores_evento; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.organizadores_evento (
    organizador_id bigint NOT NULL,
    evento_id bigint NOT NULL
);


--
-- TOC entry 194 (class 1259 OID 249374)
-- Name: participacao_evento; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.participacao_evento (
    id bigint NOT NULL,
    papel character varying(255),
    evento_id bigint,
    pessoa_id bigint
);


--
-- TOC entry 193 (class 1259 OID 249372)
-- Name: participacao_evento_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.participacao_evento_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2222 (class 0 OID 0)
-- Dependencies: 193
-- Name: participacao_evento_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.participacao_evento_id_seq OWNED BY public.participacao_evento.id;


--
-- TOC entry 196 (class 1259 OID 249382)
-- Name: participacao_trabalho; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.participacao_trabalho (
    id bigint NOT NULL,
    papel character varying(255) NOT NULL,
    pessoa_id bigint,
    trabalho_id bigint
);


--
-- TOC entry 195 (class 1259 OID 249380)
-- Name: participacao_trabalho_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.participacao_trabalho_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2223 (class 0 OID 0)
-- Dependencies: 195
-- Name: participacao_trabalho_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.participacao_trabalho_id_seq OWNED BY public.participacao_trabalho.id;


--
-- TOC entry 176 (class 1259 OID 27501)
-- Name: pessoa; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.pessoa (
    id bigint NOT NULL,
    cpf character varying(255),
    email character varying(255) NOT NULL,
    nome character varying(255) NOT NULL,
    password character varying(255)
);


--
-- TOC entry 177 (class 1259 OID 27507)
-- Name: pessoa_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.pessoa_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2224 (class 0 OID 0)
-- Dependencies: 177
-- Name: pessoa_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.pessoa_id_seq OWNED BY public.pessoa.id;


--
-- TOC entry 178 (class 1259 OID 27509)
-- Name: revisao; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.revisao (
    id bigint NOT NULL,
    conteudo text,
    revisor_id bigint,
    trabalho_id bigint,
    observacoes text,
    avaliacao character varying(30),
    indicacao boolean,
    comentarios character varying,
    avaliacao_geral text,
    auto_avaliacao character varying,
    originalidade character varying,
    merito character varying,
    clareza character varying,
    qualidade character varying,
    relevancia character varying,
    formatacao character varying,
    conteudo_json json,
    arquivo_id integer
);


--
-- TOC entry 179 (class 1259 OID 27512)
-- Name: revisao_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.revisao_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2225 (class 0 OID 0)
-- Dependencies: 179
-- Name: revisao_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.revisao_id_seq OWNED BY public.revisao.id;


--
-- TOC entry 191 (class 1259 OID 249325)
-- Name: revisores_evento; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.revisores_evento (
    revisor_id bigint NOT NULL,
    evento_id bigint NOT NULL
);


--
-- TOC entry 189 (class 1259 OID 249299)
-- Name: revisores_trablho; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.revisores_trablho (
    revisor_id bigint NOT NULL,
    trabalho_id bigint NOT NULL
);


--
-- TOC entry 186 (class 1259 OID 73648)
-- Name: secao; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.secao (
    id bigint NOT NULL,
    nome character varying(255),
    responsavel_id bigint,
    evento_id bigint,
    data_secao character varying(255),
    horario character varying(255),
    local character varying(255)
);


--
-- TOC entry 187 (class 1259 OID 73686)
-- Name: secao_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.secao_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2226 (class 0 OID 0)
-- Dependencies: 187
-- Name: secao_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.secao_id_seq OWNED BY public.secao.id;


--
-- TOC entry 180 (class 1259 OID 27514)
-- Name: submissao; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.submissao (
    id bigint NOT NULL,
    data_submissao date,
    tipo_submissao character varying(255),
    trabalho_id bigint
);


--
-- TOC entry 181 (class 1259 OID 27517)
-- Name: submissao_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.submissao_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2227 (class 0 OID 0)
-- Dependencies: 181
-- Name: submissao_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.submissao_id_seq OWNED BY public.submissao.id;


--
-- TOC entry 197 (class 1259 OID 249388)
-- Name: token; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.token (
    token character varying(255) NOT NULL,
    acao character varying(255),
    pessoa_id bigint
);


--
-- TOC entry 182 (class 1259 OID 27519)
-- Name: trabalho; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.trabalho (
    id bigint NOT NULL,
    titulo character varying(255) NOT NULL,
    evento_id bigint,
    trilha_id bigint,
    path character varying(100),
    secao_id bigint,
    status character varying(255),
    status_apresentacao boolean DEFAULT false NOT NULL,
    orientador_id integer,
    autor_id integer,
    arquivo_id integer,
    modalidade_submissao_id bigint,
    modalidade_apresentacao_id bigint,
    resumo text,
    criada_em date,
    atualizada_em date,
    palavras_chave text
);


--
-- TOC entry 183 (class 1259 OID 27522)
-- Name: trabalho_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.trabalho_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2228 (class 0 OID 0)
-- Dependencies: 183
-- Name: trabalho_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.trabalho_id_seq OWNED BY public.trabalho.id;


--
-- TOC entry 184 (class 1259 OID 27524)
-- Name: trilha; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.trilha (
    id bigint NOT NULL,
    nome character varying(255) NOT NULL,
    evento_id bigint
);


--
-- TOC entry 185 (class 1259 OID 27527)
-- Name: trilha_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.trilha_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2229 (class 0 OID 0)
-- Dependencies: 185
-- Name: trilha_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.trilha_id_seq OWNED BY public.trilha.id;


--
-- TOC entry 1998 (class 2604 OID 266277)
-- Name: arquivo id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.arquivo ALTER COLUMN id SET DEFAULT nextval('public.arquivo_id_seq'::regclass);


--
-- TOC entry 1988 (class 2604 OID 27529)
-- Name: evento id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.evento ALTER COLUMN id SET DEFAULT nextval('public.evento_id_seq'::regclass);


--
-- TOC entry 1999 (class 2604 OID 266329)
-- Name: modalidade id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.modalidade ALTER COLUMN id SET DEFAULT nextval('public.modalidade_id_seq'::regclass);


--
-- TOC entry 1996 (class 2604 OID 249377)
-- Name: participacao_evento id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.participacao_evento ALTER COLUMN id SET DEFAULT nextval('public.participacao_evento_id_seq'::regclass);


--
-- TOC entry 1997 (class 2604 OID 249385)
-- Name: participacao_trabalho id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.participacao_trabalho ALTER COLUMN id SET DEFAULT nextval('public.participacao_trabalho_id_seq'::regclass);


--
-- TOC entry 1989 (class 2604 OID 27533)
-- Name: pessoa id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pessoa ALTER COLUMN id SET DEFAULT nextval('public.pessoa_id_seq'::regclass);


--
-- TOC entry 1990 (class 2604 OID 27534)
-- Name: revisao id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.revisao ALTER COLUMN id SET DEFAULT nextval('public.revisao_id_seq'::regclass);


--
-- TOC entry 1995 (class 2604 OID 73688)
-- Name: secao id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.secao ALTER COLUMN id SET DEFAULT nextval('public.secao_id_seq'::regclass);


--
-- TOC entry 1991 (class 2604 OID 27535)
-- Name: submissao id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.submissao ALTER COLUMN id SET DEFAULT nextval('public.submissao_id_seq'::regclass);


--
-- TOC entry 1992 (class 2604 OID 27536)
-- Name: trabalho id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.trabalho ALTER COLUMN id SET DEFAULT nextval('public.trabalho_id_seq'::regclass);


--
-- TOC entry 1994 (class 2604 OID 27537)
-- Name: trilha id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.trilha ALTER COLUMN id SET DEFAULT nextval('public.trilha_id_seq'::regclass);


--
-- TOC entry 2206 (class 0 OID 266267)
-- Dependencies: 198
-- Data for Name: arquivo; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 2230 (class 0 OID 0)
-- Dependencies: 199
-- Name: arquivo_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.arquivo_id_seq', 1, false);


