package com.ot.leap.ReactiveService;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * Created by ganesr1 on 9/8/17.
 */
@Data
@AllArgsConstructor
public class AuditEvent {
    String id;
    String user;
    String eventData;
    Date timeStamp;
}
