create table cart
(
    ID int not null
        primary key
);

create table category
(
    ID   int          not null
        primary key,
    NAME varchar(255) not null
);

create table item
(
    ID         int          not null
        primary key,
    DTYPE      varchar(31)  null,
    AMOUNT     int          not null,
    PRODUCT_ID int          not null,
    INFO       varchar(255) null,
    cart_id    int          null,
    order_id   int          null
);

create table product
(
    ID     int          not null
        primary key,
    AMOUNT int          not null,
    NAME   varchar(255) not null,
    PRICE  double       not null
);

create table product_category
(
    Product_ID    int not null,
    categories_ID int not null,
    primary key (Product_ID, categories_ID),
    constraint FK_product_CATEGORY_Product_ID
        foreign key (Product_ID) references product (ID),
    constraint FK_product_CATEGORY_categories_ID
        foreign key (categories_ID) references category (ID)
);

create table schedule
(
    ID      int not null
        primary key,
    WORK_ID int not null
);

create table sequence
(
    SEQ_NAME  varchar(50) not null
        primary key,
    SEQ_COUNT decimal(38) null
);

create table service
(
    ID    int          not null
        primary key,
    NAME  varchar(255) not null,
    PRICE double       not null
);

create table serviceitem
(
    ID             int not null
        primary key,
    AMOUNT         int not null,
    AUTOSERVICE_ID int not null,
    constraint FK_serviceitem_AUTOSERVICE_ID
        foreign key (AUTOSERVICE_ID) references service (ID)
);

create table serviceuser
(
    ID          int          not null
        primary key,
    FIRSTNAME   varchar(255) not null,
    LASTNAME    varchar(255) not null,
    MONEY       double       not null,
    PASSWORD    varchar(255) not null,
    ROLE        varchar(255) null,
    USERNAME    varchar(255) not null,
    CART_ID     int          null,
    SCHEDULE_ID int          null,
    constraint USERNAME
        unique (USERNAME),
    constraint FK_serviceuser_CART_ID
        foreign key (CART_ID) references cart (ID),
    constraint FK_serviceuser_SCHEDULE_ID
        foreign key (SCHEDULE_ID) references schedule (ID)
);

create table repair
(
    ID              int          not null
        primary key,
    CAR             varchar(255) not null,
    REPAIRSTATUS    varchar(255) null,
    TOTALPRICE      double       not null,
    CLIENT_ID       int          not null,
    NOTIFICATION_ID int          null,
    WORK_ID         int          null,
    constraint FK_repair_CLIENT_ID
        foreign key (CLIENT_ID) references serviceuser (ID)
);

create table repair_serviceitem
(
    Repair_ID   int not null,
    services_ID int not null,
    primary key (Repair_ID, services_ID),
    constraint FK_repair_serviceitem_Repair_ID
        foreign key (Repair_ID) references repair (ID),
    constraint FK_repair_serviceitem_services_ID
        foreign key (services_ID) references serviceitem (ID)
);

