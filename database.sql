create database db_tpst;

use db_tpst;

create table bagian
(
    id               int auto_increment                 not null,
    nama_bagian      varchar(100)                       not null,
    singkatan_bagian varchar(20)                        not null,
    created_date     datetime default CURRENT_TIMESTAMP not null,
    updated_date     datetime default null,
    primary key (id),
    unique (singkatan_bagian)
) engine InnoDB;

insert into bagian (id, nama_bagian, singkatan_bagian)
VALUES (1, 'Administrator', 'ADMIN'),
       (2,'Bagian Umum', 'UMUM'),
       (3,'Bidang DP3', 'DP3'),
       (4,'Bidang KBP', 'KBP'),
       (5,'Bidang PPIP', 'PPIP'),
       (6,'Bidang P2Humas', 'P2Humas'),
       (7,'Bidang PEP', 'PEP');

create table kelompok
(
    id            int auto_increment                 not null,
    nama_kelompok varchar(100)                       not null,
    created_date  datetime default CURRENT_TIMESTAMP not null,
    updated_date  datetime default null,
    primary key (id)
) engine InnoDB;

insert into kelompok (id,nama_kelompok)
values (1,'admin'),
       (2,'user');

create table users
(
    id_user          varchar(9)                             not null,
    nama_user        varchar(100)                           not null,
    password         varchar(100)                           not null,
    id_bagian        int                                    not null,
    id_kelompok      int                                    not null,
    token            varchar(100) default null,
    token_expired_at bigint       default null,
    created_date     datetime     default CURRENT_TIMESTAMP not null,
    updated_date     datetime     default null,
    primary key (id_user),
    unique (token),
    foreign key fk_bagian_user (id_bagian) references bagian (id),
    foreign key fk_kelompok_user (id_kelompok) references kelompok (id)
) engine InnoDB;

create table ekspedisi
(
    id             int auto_increment                 not null,
    nama_ekspedisi varchar(100)                       not null,
    created_date   datetime default CURRENT_TIMESTAMP not null,
    updated_date   datetime default null,
    primary key (id),
    unique (nama_ekspedisi)
) engine InnoDB;

insert into ekspedisi (id, nama_ekspedisi)
values (1, 'Langsung'),
       (2, 'POS Indonesia'),
       (3, 'J&T'),
       (4, 'SAP Express'),
       (5, 'Anteraja'),
       (6, 'Pengiriman Barang Paxel'),
       (7, 'Grab Xpress'),
       (8, 'Ninja Express'),
       (9, 'Lion Parcel'),
       (10, 'SiCepat'),
       (11, 'TIKI'),
       (12, 'JNE'),
       (13, 'GoSend');

create table pengirim
(
    id            int auto_increment                 not null,
    nama_pengirim varchar(100)                       not null,
    created_date  datetime default CURRENT_TIMESTAMP not null,
    updated_date  datetime default null,
    primary key (id),
    unique (nama_pengirim)
) engine InnoDB;

create table posisi_surat
(
    id                int auto_increment                 not null,
    keterangan_posisi varchar(100)                       not null,
    created_date      datetime default CURRENT_TIMESTAMP not null,
    updated_date      datetime default null,
    primary key (id),
    unique (keterangan_posisi)
) engine InnoDB;

insert into posisi_surat (id, keterangan_posisi)
values (1, 'TPST'),
       (2, 'Bidang');

create table status
(
    id           int                                not null,
    nama_status  varchar(50)                        not null,
    created_date datetime default CURRENT_TIMESTAMP not null,
    updated_date datetime default null,
    primary key (id),
    unique (nama_status)
) engine InnoDB;

insert into status(id, nama_status)
values (0, 'delete'),
       (1, 'active');

create table surat
(
    id                    int auto_increment                 not null,
    nomor_surat           varchar(100),
    id_pengirim           int,
    perihal               varchar(255),
    id_bagian             int,
    id_posisi             int,
    id_ekspedisi          int,
    nomor_seri_ekspedisi  varchar(100),
    kontak                varchar(25),
    id_petugas_tpst       varchar(10),
    tanggal_terima_bidang datetime,
    id_petugas_bidang     varchar(10),
    nama_berkas           varchar(100),
    id_status             int      default 1,
    created_date          datetime default CURRENT_TIMESTAMP not null,
    updated_date          datetime default null,
    primary key (id),
    foreign key fk_pengirim_surat (id_pengirim) references pengirim (id),
    foreign key fk_bagian_surat (id_bagian) references bagian (id),
    foreign key fk_posisi_surat (id_posisi) references posisi_surat (id),
    foreign key fk_ekspedisi_surat (id_ekspedisi) references ekspedisi (id),
    foreign key fk_petugas_tpst_surat (id_petugas_tpst) references users (id_user),
    foreign key fk_petugas_bidang_surat (id_petugas_bidang) references users (id_user),
    foreign key fk_status_surat (id_status) references status (id)
) engine InnoDB;

create table keterangan
(
    id              int auto_increment                 not null,
    nama_keterangan varchar(30)                        not null,
    created_date    datetime default CURRENT_TIMESTAMP not null,
    update_date     datetime default null,
    primary key (id)
) engine InnoDB;

insert into keterangan(id, nama_keterangan)
values (1, 'cetak tanda terima'),
       (2, 'upload scan berkas'),
       (3, 'diubah'),
       (4, 'dihapus'),
       (5, 'diterima bidang');

create table detail_surat
(
    id            int auto_increment                 not null,
    id_surat      int                                not null,
    id_keterangan int                                not null,
    id_user       varchar(10)                        not null,
    created_date  datetime default CURRENT_TIMESTAMP not null,
    primary key (id),
    foreign key fk_surat_detail (id_surat) references surat (id),
    foreign key fk_keterangan_detail (id_keterangan) references keterangan (id),
    foreign key fk_user_detail (id_user) references users (id_user)
) engine InnoDB;

desc surat;

delete from users where id_user = '111111111';
update users set token = null, token_expired_at = null
where id_user ='111111111';
delete from detail_surat;
delete from surat;

select * from users;
select * from surat;
select
    *
from surat
where date(created_date) = '2024-11-28;'