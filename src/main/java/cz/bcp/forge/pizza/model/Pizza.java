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
import cz.bcp.forge.pizza.model.Topping;
import java.util.Set;
import java.util.HashSet;
import javax.persistence.ManyToMany;
import cz.bcp.forge.pizza.model.Base;
import javax.persistence.ManyToOne;

@Entity
public class Pizza implements Serializable
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
   private Float price;

   @ManyToMany
   private Set<Topping> toppings = new HashSet<Topping>();

   @ManyToOne
   private Base base;

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
         return id.equals(((Pizza) that).id);
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

   public Float getPrice()
   {
      return this.price;
   }

   public void setPrice(final Float price)
   {
      this.price = price;
   }

   public String toString()
   {
      String result = "";
      if (name != null && !name.trim().isEmpty())
         result += name;
      if (price != null)
         result += " " + price;
      return result;
   }

   public Set<Topping> getToppings()
   {
      return this.toppings;
   }

   public void setToppings(final Set<Topping> toppings)
   {
      this.toppings = toppings;
   }

   public Base getBase()
   {
      return this.base;
   }

   public void setBase(final Base base)
   {
      this.base = base;
   }
}