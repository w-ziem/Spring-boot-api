create table carts
(
    id          binary(16) not null default (uuid_to_bin(uuid()))
        primary key,
    date_created date not null default (CURRENT_DATE())
);

create table cartitems
(
    id       bigint auto_increment
        primary key,
    cart     binary(16) not null,
    product  bigint not null,
    quantity INTEGER not null,
    constraint cartitem_cart_id_fk
        foreign key (cart) references carts (id),
    constraint cartitem_products_id_fk
        foreign key (product) references products (id)
);