#!/bin/bash
# =============================================================================
# aplicar-comentarios.sh
#
# Reemplaza los archivos originales del proyecto con las versiones comentadas.
# Ejecutar desde la RAÍZ del proyecto (donde está el pom.xml).
#
# Uso:
#   1. Coloca proyecto-comentado.zip en la raíz del proyecto
#   2. chmod +x aplicar-comentarios.sh
#   3. ./aplicar-comentarios.sh
# =============================================================================

set -e  # Detener si algún comando falla

VERDE="\033[0;32m"
AMARILLO="\033[1;33m"
ROJO="\033[0;31m"
RESET="\033[0m"

echo ""
echo "============================================="
echo "  Aplicando comentarios al proyecto"
echo "============================================="
echo ""

# ── 1. Verificar que estamos en la raíz del proyecto ──────────────────────────
if [ ! -f "pom.xml" ]; then
  echo -e "${ROJO}ERROR: No se encontró pom.xml."
  echo -e "Ejecuta este script desde la raíz del proyecto.${RESET}"
  exit 1
fi

# ── 2. Verificar que el zip existe ────────────────────────────────────────────
if [ ! -f "proyecto-comentado.zip" ]; then
  echo -e "${ROJO}ERROR: No se encontró proyecto-comentado.zip en este directorio.${RESET}"
  exit 1
fi

# ── 3. Crear backup del código original ───────────────────────────────────────
BACKUP="backup-original-$(date +%Y%m%d-%H%M%S)"
echo -e "${AMARILLO}► Creando backup en: $BACKUP/${RESET}"
mkdir -p "$BACKUP"
cp -r src "$BACKUP/"
echo -e "${VERDE}  Backup creado correctamente.${RESET}"
echo ""

# ── 4. Extraer archivos comentados ────────────────────────────────────────────
echo -e "${AMARILLO}► Extrayendo archivos comentados...${RESET}"
TMP_DIR=$(mktemp -d)
unzip -q proyecto-comentado.zip -d "$TMP_DIR"
echo -e "${VERDE}  Extracción completada.${RESET}"
echo ""

# ── 5. Copiar sobre el proyecto original ──────────────────────────────────────
echo -e "${AMARILLO}► Copiando archivos comentados al proyecto...${RESET}"

SOURCE_DIR="$TMP_DIR/commented"

# Copiar cada archivo manteniendo la estructura de directorios
find "$SOURCE_DIR/src" -name "*.java" | while read -r file; do
  # Obtener ruta relativa desde commented/
  relative="${file#$SOURCE_DIR/}"
  target="./$relative"
  target_dir="$(dirname "$target")"

  # Crear directorio si no existe
  mkdir -p "$target_dir"

  # Copiar el archivo
  cp "$file" "$target"
  echo -e "  ${VERDE}✓${RESET} $relative"
done

echo ""

# ── 6. Limpiar temporal ───────────────────────────────────────────────────────
rm -rf "$TMP_DIR"

# ── 7. Resumen ────────────────────────────────────────────────────────────────
TOTAL=$(find src -name "*.java" | wc -l | tr -d ' ')
echo "============================================="
echo -e "${VERDE}  ¡Comentarios aplicados correctamente!"
echo -e "  Archivos Java en el proyecto: $TOTAL"
echo ""
echo -e "  Backup del original: $BACKUP/"
echo -e "  Para revertir: cp -r $BACKUP/src ."
echo -e "${RESET}============================================="
echo ""
