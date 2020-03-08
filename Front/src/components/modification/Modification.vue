<template>
  <div id="app">
    <div class="menu">
        <nav id="sidebar">
          <h1><a href="index.html" >PING 31</a></h1>
          <hr>
          <ul class="list-unstyled components mb-5">
            <li class="active">
              <a @click="$goto('/Recherche')"><span class="fa fa-home mr-3"></span> Accueil</a>
             <hr>
             </li>
            <li>
              <a @click="$goto('/Creation')"><span class="fa fa-plus mr-3"></span>Creer Message</a>
               <hr>
            </li>
          </ul>

        </nav>
      </div>
    <div class="content">
      <div class="barre">
      <ul class="barre-ul">
        <li><a class="active" href="#home">Modification Message</a></li>
      </ul>
      </div>
      <div class="col-sm-10">
      <div class="titleprghp">
        <span class="prg">   </span>  
      </div>
        

        <div class="col-sm-10">
        </div>
        <form id="app" @submit="checkForm"  method="post">
          <div class="form-group row">
            <label for="libelle" class="col-sm-2 col-form-label">Libelle:</label>
            <div class="col-sm-10">
              <input type="libelle" class="form-control" id="libelle" v-model="libelle" v-bind:placeholder=info.libelle>
            </div>
          </div>

          <fieldset class="form-group">
            <div class="row">
              <legend class="col-form-label col-sm-2 pt-0">Type de Message:</legend>
              <div class="col-sm-10">
                <div class="form-check-inline">
                  <input class="form-check-input" type="radio"  v-bind:value="type"  name="gridRadios" id="gridRadios1" value="option1">
                  <label class="form-check-label" for="gridRadios1">
                    Information
                  </label>
                </div>
                <div class="form-check-inline">
                  <input class="form-check-input" type="radio" v-model="type"  name="gridRadio2" id="gridRadios2" value="option2">
                  <label class="form-check-label" for="gridRadios2">
                    Bulle
                  </label>
                </div>
                <div class="form-check">
                  <input class="form-check-input" type="checkbox" v-model="vision360" value="" id="defaultCheck1">
                  <label class="form-check-label" for="defaultCheck1">
                    Restituer ce message sur la Vision 360
                  </label>
                </div>
              </div>
            </div>
          </fieldset>

          <div class="form-group row">
            <div class="col-sm-2">Texte:</div>
              <div class="col-sm-10">
                <textarea class="form-control" v-model="textMessage" v-bind:placeholder=info.textMessage id="txt" rows="3"></textarea>
              </div>
            </div>

          <div class="form-group row">
            <label for="motcles" class="col-sm-2 col-form-label">Mot(s) Cle(s):</label>
            <div class="col-sm-10">
              <input type="motcle" v-model="motcle" v-bind:placeholder=info.keywords class="form-control" id="motcle">
            </div>
          </div>

          <div class="form-group row">
            <label for="debut" class="col-sm-2 col-form-label">Debut d'Affichage:</label>
            <div class="col-sm-10">
              <input class="form-control" v-model="start" id="date" name="date" v-bind:placeholder=info.start type="date"/>
            </div>
          </div>

          <div class="form-group row">
            <label for="fin" class="col-sm-2 col-form-label">Fin d'Affichage:</label>
            <div class="col-sm-10">
              <input class="form-control" v-model="end" id="date" name="date" v-bind:placeholder=info.end type="date"/>
            </div>
          </div>

          <fieldset class="form-group">
            <div class="row">
              <legend class="col-form-label col-sm-2 pt-0">Entite(s) Concernee(s):</legend>
              <div class="col-sm-10">
                <div class="form-check-inline">
                  <input class="form-check-input" v-model="entity" type="checkbox" value="1" id="defaultCheck1">
                  <label class="form-check-label" for="defaultCheck1">CMB</label>
                </div>
                <div class="form-check-inline">
                  <input class="form-check-input" v-model="entity" type="checkbox" value="2" id="defaultCheck1">
                  <label class="form-check-label" for="defaultCheck1">CMMC</label>
                </div>
                <div class="form-check-inline">
                  <input class="form-check-input" v-model="entity" type="checkbox" value="3" id="defaultCheck1">
                  <label class="form-check-label" for="defaultCheck1">CMSO</label>
                </div>
                <div class="form-check-inline">
                  <input class="form-check-input" v-model="entity" type="checkbox" value="4" id="defaultCheck1">
                  <label class="form-check-label" for="defaultCheck1">BPE</label>
                </div>
                <div class="form-check-inline">
                  <input class="form-check-input" v-model="entity" type="checkbox" value="5" id="defaultCheck1">
                  <label class="form-check-label" for="defaultCheck1">ABP</label>
                </div>
              </div>
            </div>
          </fieldset>

          <fieldset class="form-group">
            <div class="row">
              <legend class="col-form-label col-sm-2 pt-0">Canaux(s) Concerne(s):</legend>
              <div class="col-sm-10" >
                <div class="form-check-inline">
                  <input class="form-check-input" v-model="canals" type="checkbox" id="defaultCheck1" value="WEB">
                  <label class="form-check-label" for="defaultCheck1">WEB</label>
                </div>
                <div class="form-check-inline">
                  <input class="form-check-input" v-model="canals" type="checkbox" id="checkgab" value="GAB">
                  <label class="form-check-label" for="checkgab">GAB</label>
                </div>

                <div class="form-check-inline">
                  <input class="form-check-input" v-model="canals" type="checkbox" value="RWD" id="defaultCheck1" >
                  <label class="form-check-label" for="defaultCheck1">RWD</label>
                </div>
              </div>
            </div>
          </fieldset>

          <div id="priorite" style="display: none" class="form-group row">
            <label for="priorite" class="col-sm-2 col-form-label">Priorite GAB:</label>
            <div class="col-sm-10">
              <input type="priorite" v-model="priority" class="form-control" id="priorite">
            </div>
          </div>
          <div class="form-group row" style="display: none" id="cacherbulle">
            <label for="libellem" class="col-sm-2 col-form-label">Libelle Message Conseiller :</label>
            <div class="col-sm-10">
              <p>Votre Conseiller vous informe</p>
            </div>
          </div>
          <div class="card" style="width: 30rem; background-color : yellow;">
          <fieldset class="form-group">
            <div class="row">
              <legend class="col-form-label col-sm-2 pt-0"> Federation:</legend>
              <div class="col-sm-10">
                <div class="form-check-inline">
                  <input class="form-check-input" type="checkbox" id="fede1">
                  <label class="form-check-label" for="defaultCheck1">CMB</label>
                </div>
                <div class="form-check-inline">
                  <input class="form-check-input" type="checkbox" value="" id="fede2">
                  <label class="form-check-label" for="defaultCheck1">CMSO</label>
                </div>
              </div>
            </div>
          </fieldset>

          <fieldset class="form-group">
            <div class="row" id="cible" style="display:none">
              <legend class="col-form-label col-sm-2 pt-0"> Cible Client:</legend>
              <div class="col-sm-10">
                <div class="form-check-inline">
                  <input class="form-check-input" type="checkbox" id="agence">
                  <label class="form-check-label" for="defaultCheck1">Une CCM/Agence</label>
                </div>
                <div class="form-check-inline">
                  <input class="form-check-input" v-model="clientList" type="checkbox" value="" id="clients">
                  <label class="form-check-label" for="defaultCheck1">Une Liste de Clients</label>
                </div>
              </div>
            </div>
          </fieldset>
          
          <div class="form-group" style="display:none" id="agencearea">
          <select multiple class="form-control" id="exampleFormControlSelect2">
          <option>Rouen , 123456</option>
          <option>Paris , 457354</option>
          <option>Caen , 998877</option>
          <option>Marseille , 77665544</option>
          <option>Lyon , 443322</option>
          </select>
          </div>

          <div class="form-group" style="display:none" id="listeclient">
          <label for="exampleFormControlFile1">Veuillez deposer la liste</label>
          <input type="file" class="form-control-file" id="exampleFormControlFile1">
          </div>
          </div>
          <div class="form-group row">
            <div class="col-sm-10">
              <button type="submit" class="btn btn-primary">Annuler</button>
              <button type="submit" class="btn btn-primary">Enregistrer</button>
            </div>
          </div>
        </form>
      </div>
  </div>
  </div>
</template>

<script type="text/javascript">
import axios from 'axios'
  $(function () {
        $("#checkgab").click(function () {
            if ($(this).is(":checked")) {
                $("#priorite").show();
            } else {
                $("#priorite").hide();
            }
        });
    });


   $(function() {
   $("input[name='gridRadio2']").click(function() {
     if ($("#gridRadios2").is(":checked")) {
       $("#cacherbulle").show();
     } else {
       $("#cacherbulle").hide();
     }
   });
 });

 $(function () {
  $(":checkbox").click(function (){
    if($("#fede1").is(":checked")&& $("#fede2").is(":checked"))  {
      $("#cible").hide();
      } else if ($("#fede1").is(":checked")|| $("#fede2").is(":checked")){
        $("#cible").show();
          } else {
            $("#cible").hide();
            }
 
        });
    });

     $(function () {
        $("#agence").click(function () {
            if ($(this).is(":checked")) {
                $("#agencearea").show();
            } else {
                $("#agencearea").hide();
            }
        });
    });

    $(function () {
        $("#clients").click(function () {
            if ($(this).is(":checked")) {
                $("#listeclient").show();
            } else {
                $("#listeclient").hide();
            }
        });
    });
export default {
  name: 'Modification',
  data: () => {
    return {
      id : '',
      type : '',
      libelle : '',
      textMessage : '',
      motcle :'' ,
      start : '',
      end : '',
      entity : [''],
      canals : [''],
      priority : '',
      priority : '',
      target : '',
      info: {},
      vision360 : '',
      clientList : ''
    }
  },
  mounted () {
    axios
      .get('http://localhost:8080/message/2052')
      .then(response => (this.info = response.data))
    this.updateMessage()
    
  },
  methods: {
    checkForm: function (e) {
      this.updateMessage()
      this.put()
      console.log("In checkform", this.info.keywords)
      console.log("In checkform text", this.info.textMessage)
      alert(this.info.keywords)
      console.log("In checkform motcles", this.motcles)
      e.preventDefault()
      this.$goto('/Recherche')
    },

    put: function () {
      axios
        .put('http://localhost:8080/message/2053', {
          id : this.info.id,
          type : this.info.type,
          libelle : this.info.libelle,
          textMessage : this.info.textMessage,
          keywords : this.info.keywords,
          start : this.info.start,
          end : this.info.end,
          entity : this.info.entity,
          canals : this.info.canals,
          priority : this.info.priority,
          priority : this.info.priorityGAB,
          target : this.info.t
        },
        this.info.id
        ).then(response => (console.log(response.data)))

    },

    updateMessage: function () {
      axios
        .get('http://localhost:8080/message/2053')
        .then(response => (this.info = response.data))
        console.log("Mot cles before if : ", this.motcle)

      if(this.libelle){
        if (this.info.libelle !== this.libelle) {
        this.info.libelle = this.libelle
        }
      }

      if(this.type){
        if (this.info.type !== this.type) {
        this.info.type = this.type
        }
      } 
      
      if(this.textMessage){
        if (this.info.textMessage !== this.textMessage) {
        this.info.textMessage = this.textMessage
        }
      }
      
      if(this.motcle){
        if (JSON.stringify(this.info.keywords) !== JSON.stringify(this.motcle)) {
          console.log("Mot cles after if : ", this.motcles)
        this.info.keywords = this.motcles
        console.log("Mot cles after keywords : ", this.info.keywords)       
        }
      }

      if(this.start){
        if (this.info.start !== this.start) {
        this.info.start = this.start
        }
      }

      if(this.end){
        if (this.info.end !== this.end) {
        this.info.end = this.end
        }
      }

      if(this.entity){
        if (this.info.entity !== this.entity) {
        this.info.entity = this.entity
        }
      }

      if(this.canals){
        if (this.info.canals !== this.canals) {
        this.info.canals = this.canals
        }
      }

      if(this.priority){
        if (this.info.priority !== this.priority) {
        this.info.priority = this.priority
        }
      }

      if(this.agency === null){
        if (this.info.agency !== this.agency) {
        this.info.agency = this.agency
        }
      }

      if(this.clientList){
        if (this.info.clientList !== this.clientList) {
        this.info.clientList = this.clientList
        }
      }

      if(this.target){
        if (this.info.t !== this.target) {
        this.info.t = this.target
        }
      }

    }
  },
computed: {
    motcles () {
     return this.motcle.split(',')
   }

  }
}
</script>

<style>

.hide{
  display: none;
}

#app{
width: 100%;

}
 
.menu{

float: left;
width:25%;
text-align:center;
height: 740px;
background: #212121;
color: #fff;
-webkit-transition: all 0.3s;
-o-transition: all 0.3s;
transition: all 0.3s;
}
#menu .h6 {
    color: #fff; }
  #menu.active {
    margin-left: -250px; }
  #menu h1 {
    margin-bottom: 20px;
    font-weight: 700;
    font-size: 20px; }

.content{
  border:1px solid blue;
/*display:inline;*/
/*padding: 5px;*/
float: right;
width:75%;
text-align:center;
/*position: fixed;*/
}
.barre .barre-ul{
   list-style-type: none;
  margin: 0;
  padding: 0;
  overflow: hidden;
  background-color: #212121;
}
.titleprghp {
background:white;
height: 46px;
color: #000;
font-size: 23px;
height: 46px;
line-height: 40px;
padding: 0 0 0 20px;
margin: 20px 0 20px;
}
ul li {
  cursor: pointer;
  position: relative;
  padding: 12px 8px 12px 40px;
  background:#212121;
  font-size: 18px;
  transition: 0.2s;

  /* make the list items unselectable */
  -webkit-user-select: none;
  -moz-user-select: none;
  -ms-user-select: none;
  user-select: none;
}
</style>