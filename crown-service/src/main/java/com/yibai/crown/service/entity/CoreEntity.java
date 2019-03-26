package com.yibai.crown.service.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;


@Setter
@Getter
public abstract class CoreEntity {
    protected Long id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoreEntity that = (CoreEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return getClass().getName() + "{id=" + id + '}';
    }

}
