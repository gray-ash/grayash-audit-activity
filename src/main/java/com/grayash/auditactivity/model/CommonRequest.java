package com.grayash.auditactivity.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class CommonRequest implements Serializable {

   private AppData appData;
}
