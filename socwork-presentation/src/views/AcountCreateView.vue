<script>
  export default {
    data(){
      return {
        inputs:{
          username: "",
          password: ""
        }
      }
    },
    methods:{
      async submit(){
        const options = {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(this.inputs)
          
        }
        const response = await fetch('http://localhost:8080/accounts/creer-compte', options);
        if(response.ok){
          alert('Compte cree avec succes')
        }else {
          console.error('Dev is error')
        }
      }
    }
  }
</script>

<template>
  <h1>Creer un compte</h1>
  <form @submit.prevent="submit" novalidate>
    <div class="flex">
      <label for="name">User name</label>
      <input type="email" id="username" name="username" v-model="inputs.username">
      <label for="password">Password</label>
      <input type="password" id="password" name="password" v-model="inputs.password">
      <button type="submit">Creer un compte</button>
    </div>
  </form>
  <div>{{ inputs.username }}</div>
</template>
<style scoped>
.flex{
  display:flex;
  flex-direction: column;
}

input{
  margin-bottom: 1rem;
}

h1{
  color: blue;
}

label::after {
  content: "*";
  color: red;
}
</style>