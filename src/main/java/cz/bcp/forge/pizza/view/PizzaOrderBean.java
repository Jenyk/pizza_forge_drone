package cz.bcp.forge.pizza.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import cz.bcp.forge.pizza.model.PizzaOrder;

/**
 * Backing bean for PizzaOrder entities.
 * <p>
 * This class provides CRUD functionality for all PizzaOrder entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class PizzaOrderBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving PizzaOrder entities
    */

   private Long id;

   public Long getId()
   {
      return this.id;
   }

   public void setId(Long id)
   {
      this.id = id;
   }

   private PizzaOrder pizzaOrder;

   public PizzaOrder getPizzaOrder()
   {
      return this.pizzaOrder;
   }

   @Inject
   private Conversation conversation;

   @PersistenceContext(type = PersistenceContextType.EXTENDED)
   private EntityManager entityManager;

   public String create()
   {

      this.conversation.begin();
      return "create?faces-redirect=true";
   }

   public void retrieve()
   {

      if (FacesContext.getCurrentInstance().isPostback())
      {
         return;
      }

      if (this.conversation.isTransient())
      {
         this.conversation.begin();
      }

      if (this.id == null)
      {
         this.pizzaOrder = this.example;
      }
      else
      {
         this.pizzaOrder = findById(getId());
      }
   }

   public PizzaOrder findById(Long id)
   {

      return this.entityManager.find(PizzaOrder.class, id);
   }

   /*
    * Support updating and deleting PizzaOrder entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.pizzaOrder);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.pizzaOrder);
            return "view?faces-redirect=true&id=" + this.pizzaOrder.getId();
         }
      }
      catch (Exception e)
      {
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
         return null;
      }
   }

   public String delete()
   {
      this.conversation.end();

      try
      {
         this.entityManager.remove(findById(getId()));
         this.entityManager.flush();
         return "search?faces-redirect=true";
      }
      catch (Exception e)
      {
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
         return null;
      }
   }

   /*
    * Support searching PizzaOrder entities with pagination
    */

   private int page;
   private long count;
   private List<PizzaOrder> pageItems;

   private PizzaOrder example = new PizzaOrder();

   public int getPage()
   {
      return this.page;
   }

   public void setPage(int page)
   {
      this.page = page;
   }

   public int getPageSize()
   {
      return 10;
   }

   public PizzaOrder getExample()
   {
      return this.example;
   }

   public void setExample(PizzaOrder example)
   {
      this.example = example;
   }

   public void search()
   {
      this.page = 0;
   }

   public void paginate()
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();

      // Populate this.count

      CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
      Root<PizzaOrder> root = countCriteria.from(PizzaOrder.class);
      countCriteria = countCriteria.select(builder.count(root)).where(getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria).getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<PizzaOrder> criteria = builder.createQuery(PizzaOrder.class);
      root = criteria.from(PizzaOrder.class);
      TypedQuery<PizzaOrder> query = this.entityManager.createQuery(criteria.select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<PizzaOrder> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      String name = this.example.getName();
      if (name != null && !"".equals(name))
      {
         predicatesList.add(builder.like(root.<String> get("name"), '%' + name + '%'));
      }
      String address = this.example.getAddress();
      if (address != null && !"".equals(address))
      {
         predicatesList.add(builder.like(root.<String> get("address"), '%' + address + '%'));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<PizzaOrder> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back PizzaOrder entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<PizzaOrder> getAll()
   {

      CriteriaQuery<PizzaOrder> criteria = this.entityManager.getCriteriaBuilder().createQuery(PizzaOrder.class);
      return this.entityManager.createQuery(criteria.select(criteria.from(PizzaOrder.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final PizzaOrderBean ejbProxy = this.sessionContext.getBusinessObject(PizzaOrderBean.class);

      return new Converter()
      {

         @Override
         public Object getAsObject(FacesContext context, UIComponent component, String value)
         {

            return ejbProxy.findById(Long.valueOf(value));
         }

         @Override
         public String getAsString(FacesContext context, UIComponent component, Object value)
         {

            if (value == null)
            {
               return "";
            }

            return String.valueOf(((PizzaOrder) value).getId());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private PizzaOrder add = new PizzaOrder();

   public PizzaOrder getAdd()
   {
      return this.add;
   }

   public PizzaOrder getAdded()
   {
      PizzaOrder added = this.add;
      this.add = new PizzaOrder();
      return added;
   }
}