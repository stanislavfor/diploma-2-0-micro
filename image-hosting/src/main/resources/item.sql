create table item
(
    id          bigint default nextval('item_id_seq'::regclass) not null
        primary key,
    name        varchar(255)                                    not null,
    description varchar(255),
    link        varchar(255)
);

alter table item
    owner to postgres;

INSERT INTO public.item (id, name, description, link) VALUES (73, 'rrr', 'rrrr', '/images/a1b29402-950e-4fc3-a764-102042233144_pangaea.jpg');
INSERT INTO public.item (id, name, description, link) VALUES (77, '555', '555', '/images/0b9a0ed6-ac52-49bd-adde-dc629d98f6f1_2016-11-07 04.26-big.jpg');
INSERT INTO public.item (id, name, description, link) VALUES (85, 'image', 'image', '/images/91b2702a-7e95-4bda-bdf6-68b17e56ac87_s1200000.png');
INSERT INTO public.item (id, name, description, link) VALUES (71, '2', 'Схема', '/images/744d1b01-8a41-49ce-986f-8758464404ef_BguFAhaY3Ks.jpg');
INSERT INTO public.item (id, name, description, link) VALUES (72, '333444', 'Метро, МЦД, МЦК - рисунок PNG', '/images/a9308f6e-db77-4c4d-b46f-e19237397e58_mosd.png');
INSERT INTO public.item (id, name, description, link) VALUES (75, 'ROSESS', 'Roses cуществительное, мн. число. розы мн.. There are a lot of rose bushes in the garden.', '/images/42a767bb-b59f-4118-a15b-99bca99046f7_PC02_330x370.png');
