package com.Darum.Employee.Management.System.Event;

import com.Darum.Employee.Management.System.Event.Enum.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KafkaEvent<T> {
    private Event action;
    private T payload;
}
