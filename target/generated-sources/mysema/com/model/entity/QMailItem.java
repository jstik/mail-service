package com.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMailItem is a Querydsl query type for MailItem
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QMailItem extends EntityPathBase<MailItem> {

    private static final long serialVersionUID = -1195487805L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMailItem mailItem = new QMailItem("mailItem");

    public final QAbstractEntity _super = new QAbstractEntity(this);

    public final StringPath body = createString("body");

    public final StringPath cc = createString("cc");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> created = _super.created;

    public final StringPath from = createString("from");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    protected QMailStatus mailStatus;

    public final StringPath subject = createString("subject");

    public final StringPath tenantId = createString("tenantId");

    public final StringPath to = createString("to");

    public final StringPath type = createString("type");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updated = _super.updated;

    public final StringPath uuid = createString("uuid");

    public QMailItem(String variable) {
        this(MailItem.class, forVariable(variable), INITS);
    }

    public QMailItem(Path<? extends MailItem> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMailItem(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMailItem(PathMetadata metadata, PathInits inits) {
        this(MailItem.class, metadata, inits);
    }

    public QMailItem(Class<? extends MailItem> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.mailStatus = inits.isInitialized("mailStatus") ? new QMailStatus(forProperty("mailStatus"), inits.get("mailStatus")) : null;
    }

    public QMailStatus mailStatus() {
        if (mailStatus == null) {
            mailStatus = new QMailStatus(forProperty("mailStatus"));
        }
        return mailStatus;
    }

}

