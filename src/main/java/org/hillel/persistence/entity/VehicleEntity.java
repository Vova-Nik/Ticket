package org.hillel.persistence.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hillel.persistence.entity.enums.VehicleType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "vehicles")
public class VehicleEntity extends AbstractEntity<Long> {

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

    @OneToMany(mappedBy = "vehicleEntity", orphanRemoval = true)
    private List<JourneyEntity> journeys = new ArrayList<>();

    public void addJourney(final JourneyEntity journey){
        if(Objects.isNull(journey)||!journey.isValid())
            return;
        journeys.add(journey);
    }

    public VehicleEntity(final String name, final VehicleType type) {
        super.setName(name);
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
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VehicleEntity that = (VehicleEntity) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
