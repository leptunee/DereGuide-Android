#!/usr/bin/env bash
set -e
source $(dirname "$0")/config.sh

mkdir -p "${TARGET_DIR}"

EXPORT_LANG=""
for language in "${TRANSLATIONS[@]}"; do
    EXPORT_LANG="${EXPORT_LANG}-exportLanguage ${language} "
done

echo "Exporting translations..."
xcodebuild -exportLocalizations -localizationPath "${TARGET_DIR}" \
    -project "${PROJECT_FILE_PATH}" ${EXPORT_LANG}
echo "Done."
