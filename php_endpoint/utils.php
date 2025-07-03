<?php
function jsonResponse($data) {
    return json_encode($data, JSON_UNESCAPED_UNICODE);
}