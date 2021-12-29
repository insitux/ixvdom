import * as Insitux from "./insitux.js";

const e = el => document.querySelector(el);
const loaderEnv = { funcs: {}, vars: {} };
const state = new Map();

function loaderGet(key) {
  return { value: state.get(key) };
}

function loaderSet(key, val) {
  return state.set(key, val) && undefined;
}

function loaderExe(name, args) {
  if (args.length && args[0].t == "str" && args[0].v.startsWith("$")) {
    if (args.length === 1) {
      return loaderGet(`${args[0].v.substring(1)}.${name}`);
    } else {
      loaderSet(`${args[0].v.substring(1)}.${name}`, args[1]);
      return { kind: "val", value: args[1] };
    }
  }
  return { kind: "err", err: `operation ${name} does not exist` };
}

const nullVal = { kind: "val", value: { t: "null", v: undefined } };
const functions = [
  {
    name: "html",
    definition: { exactArity: 1, params: ["str"], returns: ["str"] },
    handler: params => {
      e(args[0].v).innerHTML = params[1].v;
      return nullVal;
    },
  },
];

const loadScript = async scriptEl => {
  const code = scriptEl.src
    ? await (await fetch(scriptEl.src)).text()
    : scriptEl.innerHTML;
  const errors = insitux(
    {
      ...defaultBudgets,
      env: loaderEnv,
      exe: loaderExe,
      get: loaderGet,
      set: loaderSet,
      print: str => console.log(str),
      functions,
    },
    code,
  );
  if (errors.length > 0) {
    console.log(`Insitux:
${errors.map(err => err.text).join("")}`);
  }
};

window.onload = async () => {
  const scripts = document.querySelectorAll(`script[type="text/insitux"]`);
  for (let s = 0; s < scripts.length; ++s) {
    await loadScript(scripts[s]);
  }
};
