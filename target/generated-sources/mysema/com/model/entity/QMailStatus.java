package com.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMailStatus is a Querydsl query type for MailStatus
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QMailStatus extends EntityPathBase<MailStatus> {

    private static final long serialVersionUID = -1821329758L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMailStatus mailStatus = new QMailStatus("mailStatus");

    public final QAbstractEntity _super = new QAbstractEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> created = _super.created;

    public final NumberPath<Integer> fails = createNumber("fails", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    protected QMailItem mailItem;

    public final EnumPath<MailStatus.Status> status = createEnum("status", MailStatus.Status.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updated = _super.updated;

    public QMailStatus(String variable) {
        this(MailStatus.class, forVariable(variable), INITS);
    }

    public QMailStatus(Path<? extends MailStatus> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMailStatus(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMailStatus(PathMetadata metadata, PathInits inits) {
        this(MailStatus.class, metadata, inits);
    }

    public QMailStatus(Class<? extends MailStatus> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.mailItem = inits.isInitialized("mailItem") ? new QMailItem(forProperty("mailItem"), inits.get("mailItem")) : null;
    }

    public QMailItem mailItem() {
        if (mailItem == null) {
            mailItem = new QMailItem(forProperty("mailItem"));
        }
        return mailItem;
    }

}

