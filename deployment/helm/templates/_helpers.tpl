{{- define "propertiesHash" -}}
{{- $Configmap := include (print $.Template.BasePath "/configmap.yaml") . | sha256sum -}}
{{ print $Configmap | sha256sum }}
{{- end -}}