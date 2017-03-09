import Vue from 'vue';

var formatters = {
    truncate(string, value) {
        if (string && string.length > value) {
            return string.substring(0, value) + '...';
        } else {
            return string;
        }
    },
    removeDelimiters(string) {
        if (!string) return string;
        return string.replace(/[. ,:\-\/]+/g, "");
    },
    rg(string) {
        if (!string) return string;
        var v = string.replace(/\D/g, "");
        v = v.replace(/(\d{2})(\d{3})(\d{3})(\d{1})$/, "$1.$2.$3-$4");
        return v;
    },
    cpf(string) {
        if (!string) return string;
        var v = string.replace(/\D/g, "");
        v = v.replace(/(\d{3})(\d{3})(\d{3})(\d{2})$/, "$1.$2.$3-$4");
        return v;
    },
    cnpj(string) {
        if (!string) return string;
        var v = string.replace(/\D/g, "");
        v = v.replace(/(\d{2})(\d{3})(\d{3})(\d{4})(\d{2})$/, "$1.$2.$3/$4-$5");
        return v;
    },
    phone(string) {
        if (!string) return string;
        var v = string.replace(/\D/g, "");
        v = v.replace(/(\d{2})(\d{5})(\d{4})$/, "($1) $2-$3");
        return v;
    },
    cep(string) {
        if (!string) return string;
        var v = string.replace(/\D/g, "");
        v = v.replace(/(\d{5})(\d{3})$/, "$1-$2");
        return v;
    }
};

Vue.filter('truncate', formatters.truncate);
Vue.filter('rg', formatters.rg);
Vue.filter('cpf', formatters.cpf);
Vue.filter('cnpj', formatters.cnpj);
Vue.filter('phone', formatters.phone);
Vue.filter('cep', formatters.cep);

export default formatters;