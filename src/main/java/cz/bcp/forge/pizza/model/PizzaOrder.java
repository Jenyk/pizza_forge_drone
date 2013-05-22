package cz.bcp.forge.pizza.model;

import javax.persistence.Entity;
import java.io.Serializable;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Version;
import java.lang.Override;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import cz.bcp.forge.pizza.model.Pizza;
import java.util.Set;
import java.util.HashSet;
import javax.persistence.ManyToMany;

@Entity
public class PizzaOrder implements Serializable
{

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   @Column(name = "id", updatable = false, nullable = false)
   private Long id = null;
   @Version
   @Column(name = "version")
   private int version = 0;

   @Column
   @NotNull
   @Size(min = 2)
   private String name;

   @Column
   @Size(max = 30)
   private String address;

   @Column
   private Float total;

   @Temporal(TemporalType.DATE)
   private Date deliveryDate;

   @ManyToMany
   private Set<Pizza> pizzas = new HashSet<Pizza>();

   public Long getId()
   {
      return this.id;
   }

   public void setId(final Long id)
   {
      this.id = id;
   }

   public int getVersion()
   {
      return this.version;
   }

   public void setVersion(final int version)
   {
      this.version = version;
   }

   @Override
   public boolean equals(Object that)
   {
      if (this == that)
      {
         return true;
      }
      if (that == null)
      {
         return false;
      }
      if (getClass() != that.getClass())
      {
         return false;
      }
      if (id != null)
      {
         return id.equals(((PizzaOrder) that).id);
      }
      return super.equals(that);
   }

   @Override
   public int hashCode()
   {
      if (id != null)
      {
         return id.hashCode();
      }
      return super.hashCode();
   }

   public String getName()
   {
      return this.name;
   }

   public void setName(final String name)
   {
      this.name = name;
   }

   public String getAddress()
   {
      return this.address;
   }

   public void setAddress(final String address)
   {
      this.address = address;
   }

   public Float getTotal()
   {
      return this.total;
   }

   public void setTotal(final Float total)
   {
      this.total = total;
   }

   public Date getDeliveryDate()
   {
      return this.deliveryDate;
   }

   public void setDeliveryDate(final Date deliveryDate)
   {
      this.deliveryDate = deliveryDate;
   }

   public String toString()
   {
      String result = "";
      if (name != null && !name.trim().isEmpty())
         result += name;
      if (address != null && !address.trim().isEmpty())
         result += " " + address;
      if (total != null)
         result += " " + total;
      return result;
   }

   public Set<Pizza> getPizzas()
   {
      return this.pizzas;
   }

   public void setPizzas(final Set<Pizza> pizzas)
   {
      this.pizzas = pizzas;
   }
}