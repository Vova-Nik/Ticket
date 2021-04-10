package org.hillel.persistence.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hillel.persistence.entity.enums.VehicleType;
import org.springframework.util.StringUtils;
import javax.persistence.*;
import java.util.Arrays;

//@AbstractEntity
@Entity
@Data
@NoArgsConstructor
@Table(name = "vehicles")
public class VehicleEntity extends AbstractEntity<Long>{

    @Column(name = "vehicle_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;

    @Column(name = "overal_capacity", nullable = false)
    private int overalCapacity;
    @Column(name = "econom_apacity", nullable = false)
    private int economCapacity;
    @Column(name = "busines_capacity", nullable = false)
    private int businesCapacity;
    @Column(name = "comon_capacity", nullable = false)
    private int comonCapacity;

    public VehicleEntity(String name, VehicleType type) {
        setName(name);
        vehicleType = type;

        switch (type) {
            case TRAIN:
                overalCapacity = 1000;
                economCapacity = 800;
                businesCapacity = 200;
                comonCapacity = 0;
                break;
            case BUS:
                overalCapacity = 100;
                economCapacity = 0;
                businesCapacity = 0;
                comonCapacity = 100;
                break;
            case PLANE:
                overalCapacity = 150;
                economCapacity = 100;
                businesCapacity = 50;
                comonCapacity = 0;
                break;
            case SHIP:
                overalCapacity = 1200;
                economCapacity = 800;
                businesCapacity = 300;
                comonCapacity = 100;
                break;
        }
    }
    @Override
    public boolean isValid() {
            if(!super.isValid()) return false;
            return    Arrays.stream(VehicleType.values()).anyMatch((t) -> t.equals(vehicleType));
    }
}
