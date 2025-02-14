package com.mycompany.myapp.domain;

import java.util.Objects;
import org.springframework.core.io.ByteArrayResource;

public class DownloadFile {

    transient ByteArrayResource resource;
    String name;

    public DownloadFile(ByteArrayResource resource, String name) {
        this.resource = resource;
        this.name = name;
    }

    public ByteArrayResource getResource() {
        return resource;
    }

    public void setResource(ByteArrayResource resource) {
        this.resource = resource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        DownloadFile that = (DownloadFile) object;
        return Objects.equals(resource, that.resource) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resource, name);
    }

    @Override
    public String toString() {
        return "DownloadFile{" + "resource=" + resource + ", name='" + name + '\'' + '}';
    }
}
